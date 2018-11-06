package com.ala.lam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.ala.lam.vo.UserEmailInfoVO;
import com.google.gson.Gson;





public class Oauth2Resource {

	
	
	public static AccessTokenVO getAccessTokenFromAuthCode(String code) throws Exception
	{
		
		String url = "https://www.googleapis.com/oauth2/v4/token";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		InputStream instream = null;
		 String result = null;

		post.setHeader("Content-Type", "application/x-www-form-urlencoded");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("code", code));
		urlParameters.add(new BasicNameValuePair("client_id", "208971152413-0fqu23rsim920do7tbklb99t254h92sk.apps.googleusercontent.com"));
		urlParameters.add(new BasicNameValuePair("client_secret", "57DjYZhrluQYpXdy2YvOgcUu"));
		urlParameters.add(new BasicNameValuePair("redirect_uri", "https://kt2n8fjew9.execute-api.us-east-2.amazonaws.com/dev/oauth2callback"));
		urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);
		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());

	    HttpEntity entity = response.getEntity();
	    
	    if (entity != null) {
	      instream = entity.getContent();
	      result = convertStreamToString(instream);
	    }	
	    System.out.println(result);
	    Gson gson = new Gson();
	    AccessTokenVO vo= gson.fromJson(result,AccessTokenVO.class);
		
	return vo;
	}
	
	public static void main(String [] args) throws Exception{
		//getLinkedEmailFromAccessToken("ya29.GltLBhWJZrKj_C4-3UhYDcZ-FFtxdVMK3jXAvNSF7fMG9BTOf8QF0BwJnRJYxlDmSwlZmAEpuWM4eAhR_HDdRVDCwZa4bCIN1KscLigVJOaw5_Z3I8cuEw7bEn57");
		//getAccessTokenFromAuthCode("4/jAAqi7K3RtMdGK7_tRPwvSkEuSTsbGVwj1cbHcRYEkvBo3Ep-RFOt_NnGZK5PxyEPAIVH_BAkK-W5dudhRtk5Fs");
		getAccessTokenFromRefreshToken("1/siqkLrrqxLZtIDEQdeDccXiEi5d0_R8UBjA_A4UhA6M ");
	}
	
	public static String convertStreamToString(InputStream is) {
		  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		  StringBuilder sb = new StringBuilder();
		  String line = null;
		 
		  try {
		    while ((line = reader.readLine()) != null) {
		      sb.append(line + "\n");
		    }
		  } catch (IOException e) {
		  } finally {
		    try {
		      is.close();
		    } catch (IOException e) {
		    }
		  }
		 
		  return sb.toString();
		}
	
	
	public static RefreshedAccessTokenVO getAccessTokenFromRefreshToken(String code) throws Exception
	{
		
		String url = "https://www.googleapis.com/oauth2/v4/token";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		InputStream instream = null;
		 String result = null;

		post.setHeader("Content-Type", "application/x-www-form-urlencoded");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("refresh_token", code));
		urlParameters.add(new BasicNameValuePair("client_id", "208971152413-0fqu23rsim920do7tbklb99t254h92sk.apps.googleusercontent.com"));
		urlParameters.add(new BasicNameValuePair("client_secret", "57DjYZhrluQYpXdy2YvOgcUu"));
		//urlParameters.add(new BasicNameValuePair("redirect_uri", "https://kt2n8fjew9.execute-api.us-east-2.amazonaws.com/dev/oauth2callback"));
		urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);
		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());

	    HttpEntity entity = response.getEntity();
	    
	    if (entity != null) {
	      instream = entity.getContent();
	      result = convertStreamToString(instream);
	    }	
		
	System.out.println("body"+result);
	
    Gson gson = new Gson();
    RefreshedAccessTokenVO vo= gson.fromJson(result,RefreshedAccessTokenVO.class);
    return vo;
	}
	
	
	public static String getLinkedEmailFromAccessToken(String token) throws Exception
	{
		
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
		url=url+token;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
        InputStream instream=null;
        String result=null;
		// add request header
	
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());

	    HttpEntity entity = response.getEntity();
	    
	    if (entity != null) {
	      instream = entity.getContent();
	      result = convertStreamToString(instream);
	    }	
	    System.out.println(result);
	    Gson gson = new Gson();
	    UserEmailInfoVO vo= gson.fromJson(result,UserEmailInfoVO.class);
		
	return vo.getEmail();
	}
	
}
