package com.ala.lam;


import java.security.KeyStore.Entry.Attribute;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectEntitiesRequest;
import com.amazonaws.services.comprehend.model.DetectEntitiesResult;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesRequest;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesResult;
import com.amazonaws.services.comprehend.model.Entity;
import com.amazonaws.services.comprehend.model.KeyPhrase;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.google.gson.Gson;


public  class ConsumerLambdaHandler implements RequestHandler<SQSEvent, Void>{

	AmazonComprehend comprehendClient;
	public Void handleRequest(SQSEvent event, Context context) {
		// TODO Auto-generated method stub
	    comprehendClient=AmazonComprehendClientBuilder.defaultClient();
		for(SQSMessage msg : event.getRecords()){
			
			context.getLogger().log("Inside q consumer lambda "+msg.getBody());
			Gson gson=new Gson();
			MeetingRequestVO vo=gson.fromJson(msg.getBody(), MeetingRequestVO.class);
           // System.out.println(new String(msg.getBody()));
			try
			{
			processMsg(vo,context);
			}
			catch(Exception e)
			{
				context.getLogger().log("Ex  consumer lambda "+e);
			}
        }
        return null;
		
		
	}

	private void processMsg(MeetingRequestVO vo, Context context) throws Exception {
		
		Connection con=DBConnection.getConnection();
        DetectEntitiesRequest detectEntitiesRequest = new DetectEntitiesRequest().withText(vo.getRequestText())
                .withLanguageCode("en");
        
       DetectEntitiesResult detectEntitiesResult  = comprehendClient.detectEntities(detectEntitiesRequest);
        
       List<Entity> entities= detectEntitiesResult.getEntities();
       
       
       List<String> persons=getPersonFromEntities(entities, context);
       DateEntity date =getDateFromEntities(entities,context);
       System.out.println("break1");
       EventDateTime eventTime=getStartAndEndTime(date,context);
       
       
       DetectKeyPhrasesRequest detectKeyPhrasesRequest=new DetectKeyPhrasesRequest().withText(vo.getRequestText())
    		   .withLanguageCode("en");
       DetectKeyPhrasesResult detectKeyPhrasesResult=comprehendClient. detectKeyPhrases(detectKeyPhrasesRequest);
       List<KeyPhrase> keyPhrases=detectKeyPhrasesResult.getKeyPhrases();
       
       
       String organizerEmail=ProjHandler.getCalendarEmailFromSessionID(vo.getSessionId(), con, 1, context);
       
       CreateEventVO createEvent=new CreateEventVO();
       
       createEvent.setOrganizer(organizerEmail);
       createEvent.setSummary("");
       
       String location=getLocation(entities, context);
       if(location!=null)
         createEvent.setLocation(location);
       createEvent.setDescription("");
       createEvent.setDuration(30);
       
       createEvent.setStartDate(eventTime.getStart());
       createEvent.setEndDate(eventTime.getEnd());
       
       List<Attendee> attendees=new ArrayList<Attendee>();
       for(String person:persons)
       {
    	   Attendee individual=new Attendee();
    	   individual.setName(person);
    	   
    	   attendees.add(individual);
    	   
       }
       createEvent.setAttendees(attendees);
       new CreateEventHandler().processCreateEvent(con, createEvent, context);
      //
       
     //  comprehendClient.detectKeyPhrases(arg0)
		
	}

	private EventDateTime getStartAndEndTime(DateEntity date,Context context) throws Exception {
		EventDateTime eventTime=null;
		int dayOfweek=1;
		
	
		
		if(!StringUtils.isEmpty(date.getDay()))
		{
			eventTime=new EventDateTime();
			
			context.getLogger().log("yy "+date.getDay());
			
			if(date.getDay().equals("sunday"))
				dayOfweek=1;
			else if(date.getDay().equals("monday"))
				dayOfweek=2;
			else if(date.getDay().equals("tuesday"))
				dayOfweek=3;
			else if(date.getDay().equals("wednesday"))
				dayOfweek=4;
			else if(date.getDay().equals("thurday"))
				dayOfweek=5;
			else if(date.getDay().equals("friday"))
				dayOfweek=6;
			else if(date.getDay().equals("saturday"))
				dayOfweek=7;
			

			
		   	Calendar calendar = Calendar.getInstance();
	    	int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	    	int month=calendar.get(Calendar.MONTH)+1;
	    	int year=calendar.get(Calendar.YEAR);
	    	int dateOfCal=calendar.get(Calendar.DATE);
	    	int dayDiff=0;
	    	
	    	String result=year+"-"+month+"-";
	    	
	
	    	
	    	if(dayOfweek>=currentDayOfWeek)
	    		dayDiff=dayOfweek-currentDayOfWeek;
	    	else
	    		dayDiff=dayOfweek+7-currentDayOfWeek;
	    	
	    	int  resultDate=dateOfCal+dayDiff;
	    	String str=resultDate+"";
	    	  if(str.length()==1)
	    		  str="0"+str;
	    	  
	    	  
	  
	    	
	    	result=result+str;
    		String stime="";
			String etime="";
		
			
	    	if(!StringUtils.isEmpty(date.getTimeOfDay()))
	    	{


	    		if(date.getTimeOfDay().equals("morning"))
	    		{
	    			
	    			 stime=result+"T"+"06:00:00.000";
	    			 etime=result+"T"+"12:00:00.000";
	    		}
	    		
	    		if(date.getTimeOfDay().equals("afternoon"))
	    		{
	    			
	    			 stime=result+"T"+"12:00:00.000";
	    			 etime=result+"T"+"18:00:00.000";
	    		}
	    		
	    		if(date.getTimeOfDay().equals("evening"))
	    		{
	    			
	    			 stime=result+"T"+"18:00:00.000";
	    			 etime=result+"T"+"23:59:59.000";
	    		}
	    		

	    	}
	    	else
	    	{
   			 stime=result+"T"+"00:00:00.000";
   			 etime=result+"T"+"23:59:59.000";
	    	}
	    	
	 
	    	
    		eventTime.setStart(CalendarUtils.ConvertFormattedDateToMilis(stime)-330*60*1000);
    		eventTime.setEnd(CalendarUtils.ConvertFormattedDateToMilis(etime)-330*60*1000);
    		

	    	
			
		}
		return eventTime;
	}

	private DateEntity getDateFromEntities(List<Entity> entities,Context context) {

		String d=null;
		DateEntity date=null;
		if(entities!=null)
		{
			for(Entity en:entities)
			{
				if(en.getType().toLowerCase().equals("date"))
				{
				 d=en.getText();
				 break;
				}
				
					
			}
		}
		
		if(d!=null)
		{
			
			
			date=new DateEntity();
			
			d=d.toLowerCase();
			
			if(d.contains("week"))
				date.setWeek(true);
			
			if(d.contains("sunday"))
			{
				date.setDay(true);
				date.setDay("sunady");
			}
			if(d.contains("monday"))
			{
				date.setDay(true);
				date.setDay("monday");
			}
			if(d.contains("tuesday"))
			{
				date.setDay(true);
				date.setDay("tuesday");
			}
			if(d.contains("wednesday"))
			{
				date.setDay(true);
				date.setDay("wednesday");
			}
			if(d.contains("thursday"))
			{
				date.setDay(true);
				date.setDay("thursday");
			}
			if(d.contains("friday"))
			{
				date.setDay(true);
				date.setDay("friday");
			}
			if(d.contains("saturday"))
			{
				date.setDay(true);
				date.setDay("saturday");
			}
		
			
			
			
			if(d.contains("january")||d.contains("february")||d.contains("march")||d.contains("april")
					||d.contains("may")||d.contains("june")||d.contains("july")||d.contains("august")
					||d.contains("september")||d.contains("october")||d.contains("november")||d.contains("december"))
			{
				date.setDate(true);
				date.setDate(d);
			}
			
			if(d.contains("morning"))
			{
				date.setTimeOfDay("morning");
			}
			if(d.contains("afternoon"))
			{
				date.setTimeOfDay("afternoon");
			}
			if(d.contains("evening"))
			{
				date.setTimeOfDay("evening");
			}
			
		}
		
		context.getLogger().log("break date*****");
		
		
		return date;
	}

	private List<String> getPersonFromEntities(List<Entity> entities,Context context) {
		
		List<String> names=new ArrayList<String>();
		if(entities!=null)
		{
			for(Entity en:entities)
			{
				context.getLogger().log("type "+en.getType());
				if(en.getType().toLowerCase().equals("person"))
					names.add(en.getText());
			}
		}
		return names;
	}
	
	private String getLocation(List<Entity> entities,Context context) {
		
		String location=null;
		if(entities!=null)
		{
			for(Entity en:entities)
			{
				context.getLogger().log("type "+en.getType());
				if(en.getType().toLowerCase().equals("location"))
				{
					location=en.getText();
				}
			}
		}
		return location;
	}

}
