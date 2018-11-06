package com.ala.lam;

public class LoginVO {

	private String email;
	private String password;
	private String name;
	private int isCalenderLinked;
	public int getIsCalenderLinked() {
		return isCalenderLinked;
	}
	public void setIsCalenderLinked(int isCalenderLinked) {
		this.isCalenderLinked = isCalenderLinked;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
