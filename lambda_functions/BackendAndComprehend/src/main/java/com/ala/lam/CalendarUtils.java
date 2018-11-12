package com.ala.lam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.text.DateFormatter;

public class CalendarUtils {

    public static String dateFormat = "yyyy-MM-dd hh:mm:ss.SSS";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    public static String ConvertMilliSecondsToFormattedDate(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        

        
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime()).replace(" ", "T");
    }
    
    
    public static boolean doesOverLap(long x1,long y1, long x2, long y2)
    {
    	if(y1==x2)
    		return true;
    	return !((x1 >= y1 && x1 <= y2) ||
    	         (x2 >= y1 && x2 <= y2) ||
    	         (y1 >= x1 && y1 <= x2) ||
    	         (y2 >= x1 && y2 <= x2));
    }
    
    public static long ConvertFormattedDateToMilis(String date) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	
    	TimeZone tz=TimeZone.getTimeZone("Asia/Calcutta");
    	sdf.setTimeZone(tz);
    	
    	Date result = sdf.parse(date.replace("T", " "));
    	long timeInMillis = result.getTime();
    	return timeInMillis;
    	
    	
    }
    
    public static void main(String[] args) throws ParseException
    {
/*    	System.out.println(ConvertFormattedDateToMilis("2018-11-07T12:30:00.000"));
    	System.out.println(ConvertFormattedDateToMilis("2018-11-07T15:30:00.000"));
    	
    	
    	Calendar calendar = Calendar.getInstance();
    	int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    	int month=calendar.get(Calendar.MONTH);
    	int year=calendar.get(Calendar.YEAR);
    	int date=calendar.get(Calendar.DATE);
    	System.out.println(dayOfWeek);
    	System.out.println(month);
    	System.out.println(year);
    	System.out.println(date);
    	System.out.println("********************");
    	
    	long l=System.currentTimeMillis();
    	
    	System.out.println("mil after 1970 time gmt "+l);
    	
    	System.out.println(ConvertMilliSecondsToFormattedDate(l));*/
    	System.out.println(doesOverLap(1542177000000l,1542189600000l, 1542177000000l, 1542178800000l));
   	List<String> a=new ArrayList<String>();
    	a.add("1542177000000:1542189600000");
    	a.add("1542177000000:1542178800000");

    	a=getCompactOverlappedRegion(a);
    	Collections.sort(a);
    	
    	//System.out.println(doesOverLap(500,1000,10,20));
    	System.out.println("now  "+a);
    /*	for(String s:a)
    	{
    		System.out.println(s);
    	}*/

    	Date d= new Date(1542133800000l);
    	Date t= new Date(1542198600000l);
    	System.out.println(d);
    	System.out.println(t);
    	

    	Date d1= new Date(1542133800000l);
    	Date t1= new Date(1542135600000l);
    	System.out.println(d1);
    	System.out.println(t1);
    	System.out.println(ConvertFormattedDateToMilis("2018-11-14T12:00:00.000"));
    	System.out.println(ConvertFormattedDateToMilis("2018-11-14T00:00:00.000"));
    	
    }
    
    public static List<String> getCompactOverlappedRegion(List<String>  list){

    	if(list.size()==1)
    		return list;
    	int count=0;
    	
    	for(int i=0;i<list.size();i++)
    	{  
    		long start=Long.parseLong(list.get(i).split(":")[0]);
    		long end=Long.parseLong(list.get(i).split(":")[1]);
    		long start1=0;
    		long end1=0;
    		
    	
    		
    	 	for(int j=i+1;j<list.size();j++)
        	{  
    	 	
    	 	
        		 start1=Long.parseLong(list.get(j).split(":")[0]);
        		 end1=Long.parseLong(list.get(j).split(":")[1]);
        		
        		 boolean flag=false;
        		 if(start<=start1)
        			 flag=doesOverLap(start, end, start1, end1);
        		 else
        			 flag=doesOverLap(start1, end1, start, end);
        		 
        		 System.out.println("vv "+flag);
        		 
        		if(flag)
        		{
        			 long start2=0;
        			 long end2=0;
        			 
        			 if(start<=start1)
        				 start2=start;
        			 else
        				 start2=start1;
        			 
        			 if(end<=end1)
        				 end2=end1;
        			 else
        				 end2=end;
        			 
        			 String result=start2+":"+end2;
        			 list.remove(i);
        			 list.remove(j-1);
        			 list.add(result);
        			 
        			 
        		  count++;
        		  break;
        		  
        		 
        		}
        		
        	}
    	 	
    	 	if(count>0)
    	 		break;
    	}
    	
    	if(count==0)
    		return list;
    	else 
    	{
    		return getCompactOverlappedRegion(list);
    	}
    	

    }
    


}