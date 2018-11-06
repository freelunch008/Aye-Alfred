package com.ala.lam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    	return !((x1 >= y1 && x1 <= y2) ||
    	         (x2 >= y1 && x2 <= y2) ||
    	         (y1 >= x1 && y1 <= x2) ||
    	         (y2 >= x1 && y2 <= x2));
    }
    
    public static long ConvertFormattedDateToMilis(String date) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    	
    	TimeZone tz=TimeZone.getTimeZone("Asia/Calcutta");
    	Date result = sdf.parse(date.replace("T", " "));
    	long timeInMillis = result.getTime();
    	return timeInMillis;
    	
    	
    }
    
    public static void main(String[] args) throws ParseException
    {
    	System.out.println(ConvertFormattedDateToMilis("2018-11-07T12:30:00.000"));
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
    	
    	System.out.println(ConvertMilliSecondsToFormattedDate(l));
    	
    	
    }
    


}