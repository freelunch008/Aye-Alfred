package com.ala.lam;

public class DateEntity {

	private boolean isDay;
	private boolean isWeek;
	private boolean isDate;
	private boolean isFormatDate;
	private String day;
	private String date;
	private String timeOfDay;
	
	public String getTimeOfDay() {
		return timeOfDay;
	}
	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
	public boolean isDay() {
		return isDay;
	}
	public void setDay(boolean isDay) {
		this.isDay = isDay;
	}
	public boolean isWeek() {
		return isWeek;
	}
	public void setWeek(boolean isWeek) {
		this.isWeek = isWeek;
	}
	public boolean isDate() {
		return isDate;
	}
	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}
	public boolean isFormatDate() {
		return isFormatDate;
	}
	public void setFormatDate(boolean isFormatDate) {
		this.isFormatDate = isFormatDate;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
