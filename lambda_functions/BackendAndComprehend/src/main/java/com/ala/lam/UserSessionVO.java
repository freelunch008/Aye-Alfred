package com.ala.lam;

public class UserSessionVO 
{
	
	private String userName;
	private String registerEmail;
	private int isCalenderLinked;
	private String calenderEmail;

public String getCalenderEmail() {
		return calenderEmail;
	}
	public void setCalenderEmail(String calenderEmail) {
		this.calenderEmail = calenderEmail;
	}
public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRegisterEmail() {
		return registerEmail;
	}
	public void setRegisterEmail(String registerEmail) {
		this.registerEmail = registerEmail;
	}
	public int getIsCalenderLinked() {
		return isCalenderLinked;
	}
	public void setIsCalenderLinked(int isCalenderLinked) {
		this.isCalenderLinked = isCalenderLinked;
	}

}
