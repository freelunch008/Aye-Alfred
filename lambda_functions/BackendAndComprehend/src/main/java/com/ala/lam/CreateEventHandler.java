package com.ala.lam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.google.gson.Gson;


public class CreateEventHandler {
	
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	public ApiGatewayResponse process(ApiGatewayRequest request,Context context) {
		
		Connection con=null;
		String success="{\"code\":\"0\",\"msg\":\"success\"}";
		String error="{\"code\":\"500\",\"msg\":\"error\"}";
		Map<String,String> m=new HashMap<String, String>();
		m.put("Content-Type", "application/json");
		try
		{
		con=DBConnection.getConnection();
		context.getLogger().log("entered into CreateEventHandler-req body "+request.getBody());
	    Gson gson = new Gson();
		String reqBody=request.getBody();
	    CreateEventVO cvo= gson.fromJson(reqBody,CreateEventVO.class);

	    processCreateEvent(con, cvo, context);

		}
		catch(Exception e)
		{
			context.getLogger().log("exception  CreateEventHandler: "+e);
		}
		
		
	     return new ApiGatewayResponse(200,m,success,false);
	}
	
	public  void processCreateEvent(Connection con,CreateEventVO cvo,Context context ) throws Exception
	{
		UserAccessTokenVO uvo=getAccessTokenForUserFromEmail( con, cvo.getOrganizerEmail(), context);
	    
	    crateEvent(con,cvo,uvo,context);

		
	}
	


	private UserAccessTokenVO getEmailAndAccessTokenFromName(Connection con,String name, Context context) throws Exception {
		UserAccessTokenVO vo=null;
		
		String sql="SELECT uit.name,uit.linked_calender_email,uatt.access_token,uatt.refresh_token,uit.register_email FROM user_info_tbl uit"
				+ " JOIN user_access_token_tbl uatt  "
				+ " ON uit.register_email=uatt.email"
				+ " WHERE uit.name  LIKE ?";
		

		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,"%"+name+"%");  
		ResultSet rs=null;
		rs=stmt.executeQuery();
		
		if(rs.next())
		{
			vo=new UserAccessTokenVO();
			
			vo.setName(rs.getString("name"));
			vo.setUserEmail(rs.getString("register_email"));
			vo.setCalenderEmail(rs.getString("linked_calender_email"));
			
			AccessTokenVO avo=new AccessTokenVO();
			avo.setAccess_token(rs.getString("access_token"));
			avo.setRefresh_token(rs.getString("refresh_token"));
			
			vo.setAccessTokenVO(avo);
		
		}
		else
		{
			context.getLogger().log("User does not exist ");
			throw new Exception("UserNotExist");
		}
	
		
		return vo;
	}

	public static UserAccessTokenVO getAccessTokenForUserFromEmail(Connection con,String email,Context context) throws Exception
	{
	
		UserAccessTokenVO vo=null;
		
		String sql="SELECT uatt.email,uatt.access_token,uatt.refresh_token,uit.linked_calender_email,uit.name FROM "
				+ "user_info_tbl uit JOIN user_access_token_tbl uatt "
				+ " ON uit.register_email=uatt.email  WHERE uatt.email=?";
		

		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,email);  
		ResultSet rs=null;
		rs=stmt.executeQuery();
		
		if(rs.next())
		{
			
			vo=new UserAccessTokenVO();
			
			vo.setName(rs.getString("name"));
			vo.setUserEmail(rs.getString("email"));
			vo.setCalenderEmail(rs.getString("linked_calender_email"));
			
			AccessTokenVO avo=new AccessTokenVO();
			avo.setAccess_token(rs.getString("access_token"));
			avo.setRefresh_token(rs.getString("refresh_token"));
			
			vo.setAccessTokenVO(avo);
			
		}
		else
		{
			context.getLogger().log("header value for session token does not exist in DB ");
		}
	
		
		return vo;
	}
	
	
	private void crateEvent(Connection con,CreateEventVO cvo,UserAccessTokenVO uvo, Context context) throws Exception
	{
		

		findTimeToCreateEvent4(cvo, uvo, context);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		List<Attendee> attendee=cvo.getAttendees();
		
		
		long startTime=cvo.getStartDate();
		long endTime=cvo.getEndDate();
		
		context.getLogger().log("Time range min -"+cvo.getStartDate());
		context.getLogger().log("Time range max -"+cvo.getEndDate());
		
		 Event event = new Event();
		 if(!StringUtils.isEmpty(cvo.getSummary()))
			 event.setSummary(cvo.getSummary());
		 
		 if(!StringUtils.isEmpty(cvo.getLocation()))
			 event.setLocation(cvo.getLocation());
			 
		 if(!StringUtils.isEmpty(cvo.getDescription()))	 
	            event.setDescription("");


	        	

	        	
	        	List<EventAttendee> list=new ArrayList<EventAttendee>();
	        	
	        	for(Attendee ae:attendee)
	        	{
	        		list.add(new EventAttendee().setEmail(ae.getEmail()));
	        	}



	        	
	        	
	        	
	        	event.setAttendees(list);

	        	EventReminder[] reminderOverrides = new EventReminder[] {
	        	    new EventReminder().setMethod("email").setMinutes(60),
	        	    new EventReminder().setMethod("popup").setMinutes(10),
	        	};
	        	Event.Reminders reminders = new Event.Reminders()
	        	    .setUseDefault(false)
	        	    .setOverrides(Arrays.asList(reminderOverrides));
	        	//event.setReminders(reminders)

	        	String calendarId = cvo.getOrganizerEmail();
	        	
	        	
				
				 GoogleCredential credential = null;	
			     Calendar service =null;
			     

		        
		     try
		     {
		         credential = new GoogleCredential().setAccessToken(uvo.getAccessTokenVO().getAccess_token());	
		         service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
		                credential).build();
		         
		         com.google.api.services.calendar.model.Calendar calendar =service.calendars().get("primary").execute();
		         
		        	DateTime startDateTime = new DateTime(startTime);
		        	EventDateTime start = new EventDateTime()
		        	    .setDateTime(startDateTime).setTimeZone(calendar.getTimeZone());
		        	event.setStart(start);

		        	DateTime endDateTime = new DateTime(endTime);
		        	EventDateTime end = new EventDateTime()
		        	    .setDateTime(endDateTime).setTimeZone(calendar.getTimeZone());
		        	event.setEnd(end);
		         
		         
	        	  event = service.events().insert(calendarId, event).setSendUpdates("all").setSendNotifications(true).execute();
	        	  
	
		     }
		     catch(Exception e)
		     {
		        	if(e.getMessage().trim().startsWith("401")||e.getMessage().trim().startsWith("40"))
		        	{
		        		RefreshedAccessTokenVO rvo=Oauth2Resource.getAccessTokenFromRefreshToken(uvo.getAccessTokenVO().getRefresh_token());
		        		
		        		credential = new GoogleCredential().setAccessToken(rvo.getAccess_token());	
				        service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				                credential).build();
				         com.google.api.services.calendar.model.Calendar calendar =service.calendars().get("primary").execute();
				         
				        	DateTime startDateTime = new DateTime(startTime);
				        	EventDateTime start = new EventDateTime()
				        	    .setDateTime(startDateTime).setTimeZone(calendar.getTimeZone());
				        	event.setStart(start);

				        	DateTime endDateTime = new DateTime(endTime);
				        	EventDateTime end = new EventDateTime()
				        	    .setDateTime(endDateTime).setTimeZone(calendar.getTimeZone());
				        	event.setEnd(end);
				        
				        event = service.events().insert(calendarId, event).setSendUpdates("all").setSendNotifications(true).execute();
		        	}
		        	
		     }
	        	
		

		
		
	}
	private void findTimeToCreateEvent4(CreateEventVO cvo,UserAccessTokenVO uvo, Context context) throws Exception{
		
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Connection con=DBConnection.getConnection();
		List<Attendee> attendee=cvo.getAttendees();
		
		
		long startTime=cvo.getStartDate();
		long endDate=cvo.getEndDate();
		
		  context.getLogger().log("expected date range "+startTime+ " : "+endDate);
		
		List<String> attendeeTimes=new ArrayList<String>();
		
		long eventStart=startTime;
		long eventEnd=startTime+30*60*1000;
		
		for(Attendee at:attendee)
		{

				UserAccessTokenVO attendeeVO=getEmailAndAccessTokenFromName(con,at.getName(),context);
				
				String email=attendeeVO.getCalenderEmail();
				context.getLogger().log("attendee email :"+email);
				at.setEmail(email);
				
				
				 GoogleCredential credential = null;	
			     Calendar service =null;
			     Events events=null;
			     List<Event> items=null;
			     
		        
		     try
		     {
		         credential = new GoogleCredential().setAccessToken(attendeeVO.getAccessTokenVO().getAccess_token());	
		         service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
		                credential).build();
		        
		        events=service.events().list("primary").setTimeMin(new DateTime(startTime)).
		        		setTimeMax(new DateTime(endDate)).execute();
		        
		        }
		        catch(Exception e)
		        {
		        	if(e.getMessage().trim().startsWith("401")||e.getMessage().trim().startsWith("40"))
		        	{
		        		RefreshedAccessTokenVO rvo=Oauth2Resource.getAccessTokenFromRefreshToken(attendeeVO.getAccessTokenVO().getRefresh_token());
		        		
		        		credential = new GoogleCredential().setAccessToken(rvo.getAccess_token());	
				        service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				                credential).build();
				        
				        events=service.events().list("primary").setTimeMin(new DateTime(startTime)).
				        		setTimeMax(new DateTime(endDate)).execute();
	
		        	}
		        }
		     
		        items= events.getItems();
	            if (items.isEmpty()) 
	            {
	                context.getLogger().log("No upcoming events found.");
	            } else 
	            {
	                System.out.println("Upcoming events");
	                
	                for (Event eventt : items) 
	                {
	                    DateTime start = eventt.getStart().getDateTime();      
	                    DateTime end = eventt.getEnd().getDateTime();
	                    
	                    System.out.println("Upcoming events start "+start.toString());
	                    System.out.println("Upcoming events end "+end.toString());
	                    
	                    
	                    long tmps=0;
	                    long tmpe=0;
	                    
	                    if (start != null) 
	                    {
	                    	tmps=CalendarUtils.ConvertFormattedDateToMilis(start.toString().split("\\+")[0]);

	                    }
	                    
	                    
	                    if (end != null) 
	                    {
	                    	tmpe=CalendarUtils.ConvertFormattedDateToMilis(end.toString().split("\\+")[0]);

	                    }
	                    if(tmps>0 && tmpe>0)
	                    {
	                    	attendeeTimes.add(String.valueOf(tmps)+":"+String.valueOf(tmpe));
	                    }
	                   
	                }
	              }
				
				
		//for loop attendee	
		}
		context.getLogger().log("before getCompactOverlappedRegion");
		context.getLogger().log("Array 1"+attendeeTimes);
		attendeeTimes=CalendarUtils.getCompactOverlappedRegion(attendeeTimes);
		context.getLogger().log("Array 2"+attendeeTimes);
		Collections.sort(attendeeTimes);
		context.getLogger().log("Array 3"+attendeeTimes);
		context.getLogger().log("after getCompactOverlappedRegion");
		
		if(attendeeTimes.size()>0)
		{
			
			int found=0;
			for(int i=0;i<attendeeTimes.size();i++)
			{
				if(attendeeTimes.size()==1)
				{
					if(Long.parseLong(attendeeTimes.get(0).split(":")[0])-eventStart>=30*60*1000)
					{
						break;
					}
				}
				
				
				
				for(int j=i+1;j<attendeeTimes.size();j++)
				{
					long end1=Long.parseLong(attendeeTimes.get(i).split(":")[1]);
					long start2=Long.parseLong(attendeeTimes.get(j).split(":")[0]);
					
					if((start2-end1)>=30*60*1000)
					{
						eventStart=end1;
						eventEnd=end1+30*60*1000;
						found++;
						break;
						
					}
	
				}
				
				if(found>0)
					break;

			}
			
			if(found==0)
			{
				
			 if(endDate-Long.parseLong(attendeeTimes.get(attendeeTimes.size()-1).split(":")[1])>=30*60*1000)
			 {
				 eventStart=Long.parseLong(attendeeTimes.get(attendeeTimes.size()-1).split(":")[1])	;
				 eventEnd=eventStart+30*60*1000;
			 }
			
			}
		}
		
		context.getLogger().log("resultant start time"+eventStart);
		context.getLogger().log("resultant end time"+eventEnd);
		
		cvo.setStartDate(eventStart);
		cvo.setEndDate(eventEnd);
		

		
		
	}

}
