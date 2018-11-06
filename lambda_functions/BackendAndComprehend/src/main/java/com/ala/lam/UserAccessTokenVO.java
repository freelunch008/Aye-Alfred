package com.ala.lam;

public class UserAccessTokenVO {

	private String calenderEmail;
	private String userEmail;
	private String name;
	private AccessTokenVO accessTokenVO;
	public String getCalenderEmail() {
		return calenderEmail;
	}
	public void setCalenderEmail(String calenderEmail) {
		this.calenderEmail = calenderEmail;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AccessTokenVO getAccessTokenVO() {
		return accessTokenVO;
	}
	public void setAccessTokenVO(AccessTokenVO accessTokenVO) {
		this.accessTokenVO = accessTokenVO;
	}

}
