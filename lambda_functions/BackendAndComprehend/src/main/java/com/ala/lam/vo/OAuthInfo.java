package com.ala.lam.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthInfo {

	  private String clientId = null;
	  private String clientSecret = null;
	  private String grantType = null;
	  private String authEndPoint = null;
	  private String authScope = null;
	  private String authScheme = null;
	  private String authSchemeString = null;



	@JsonProperty("client_id")  
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	@JsonProperty("client_secret")
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	@JsonProperty("grant_type")
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	@JsonProperty("auth_end_point")
	public String getAuthEndPoint() {
		return authEndPoint;
	}
	public void setAuthEndPoint(String authEndPoint) {
		this.authEndPoint = authEndPoint;
	}
	@JsonProperty("auth_scope")
	public String getAuthScope() {
		return authScope;
	}
	public void setAuthScope(String authScope) {
		this.authScope = authScope;
	}
	
	@JsonIgnore
	public String getAuthScheme() {
		return authScheme;
	}
	public void setAuthScheme(String authScheme) {
		this.authScheme = authScheme;
	}
	@JsonIgnore
	public String getAuthSchemeString() {
		return authSchemeString;
	}
	public void setAuthSchemeString(String authSchemeString) {
		this.authSchemeString = authSchemeString;
	}
	

}
