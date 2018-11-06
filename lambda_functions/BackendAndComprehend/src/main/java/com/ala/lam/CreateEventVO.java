package com.ala.lam;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class CreateEventVO {

	private String organizerEmail="";
	private long startDate;
	private long endDate;
	private int duration;
	private List<Attendee> attendees=new ArrayList<Attendee>();
	private String location="";
	private String description="";
	private String summary="";
	public String getOrganizerEmail() {
		return organizerEmail;
	}
	public void setOrganizer(String organizerEmail) {
		this.organizerEmail = organizerEmail;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public List<Attendee> getAttendees() {
		return attendees;
	}
	public void setAttendees(List<Attendee> attendees) {
		this.attendees = attendees;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public static void main(String[] args)
	{
		CreateEventVO obj=new CreateEventVO();
		
		 List<Attendee> fh=new ArrayList<Attendee>();
		 Attendee a=new Attendee();
		 a.setName("Manohar");
		 a.setEmail("abc@gmail.com");
		 fh.add(a);
		 
		 obj.setAttendees(fh);
		 
		 obj.setSummary("Orbipay OBCS Deployment");
		 obj.setLocation("Hyderabad");
		 obj.setDescription("A chance to hear more about Google's developer products.");
		
	     Gson gson = new Gson();
	        String json = gson.toJson(obj);
	        System.out.println(json);
	}
}
