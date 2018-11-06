package com.ala.lam;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;

public class LoginHandler {

	
	public ApiGatewayResponse process(ApiGatewayRequest request,Context context) {
		Connection con=null;
		String response="";
		String sid=null;
		try
		{
			
	    String body=request.getBody();
	    Gson gson = new Gson(); // Or use new GsonBuilder().create();
	    LoginVO vo = gson.fromJson(body, LoginVO.class);
			
		 con=DBConnection.getConnection();
		 boolean flag=loginUser(vo, con);
		 	 
		if(flag)
		{
			response="{\"code\":\"0\",\"msg\":\"success\"}";
			sid=createSession(vo, con);
		}
		else
		{
			response="{\"code\":\"500\",\"msg\":\"fail\"}";

		}
		
		Map<String,String> m=new HashMap<String, String>();
		m.put("Content-Type", "application/json");
		if(sid!=null)
			m.put("ABCXYZSESSION", sid);
		return new ApiGatewayResponse(200,m,response,false);
		}
		catch(Exception e)
		{
			context.getLogger().log("exception  ProjHandler: "+e);
		}
		finally
		{
			if(con!=null)
				try
			{   
				con.close();
			}
			catch(Exception e)
			{
				context.getLogger().log("exception  in close connection: "+e);
			}
		}
		
		return null;
	}
	
	private boolean loginUser(LoginVO vo, Connection con) throws Exception{
		boolean f=false;
		String sql="SELECT is_calender_linked,name FROM user_info_tbl WHERE register_email=? AND password=?";
		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,vo.getEmail());  
		stmt.setString(2,vo.getPassword());  

		ResultSet rs=stmt.executeQuery();
		
		if(rs.next())
		{
			vo.setName(rs.getString("name"));
			vo.setIsCalenderLinked(rs.getInt("is_calender_linked"));
			f=true;
		}
		
		
		return f;
	}

	private ApiGatewayResponse getHomePage(LoginVO vo,Context context) throws Exception {
		AmazonS3 client=null;
		String s="";
		Map<String,String> m=null;
		try
		{
		client=AmazonS3ClientBuilder.defaultClient();
		S3Object obj=null;
		if(vo.getIsCalenderLinked()==0)
			 obj = client.getObject("cal-website2018", "link.html");
			else
				 obj = client.getObject("cal-website2018", "event.html");
		byte[] bytes = IOUtils.toByteArray(obj.getObjectContent());
		 s= new String(bytes, StandardCharsets.UTF_8);
		
		context.getLogger().log("homepage html is: "+s);
		
	   m=new HashMap<String, String>();
		m.put("Content-Type", "text/html");

		}
		catch(Exception e)
		{		context.getLogger().log("homepage exp is: "+e);
		         throw e;
			
		}

	     return new ApiGatewayResponse(200,m,s,false);
	}
	
	
	private ApiGatewayResponse getLandPage(LoginVO vo,Context context) throws Exception{
		AmazonS3 client=null;
		String s="";
		Map<String,String> m=null;
		try
		{
		client=AmazonS3ClientBuilder.defaultClient();
		
		S3Object obj = client.getObject("cal-website2018", "index.html");
		byte[] bytes = IOUtils.toByteArray(obj.getObjectContent());
		 s= new String(bytes, StandardCharsets.UTF_8);
		
		context.getLogger().log("landpage html is: "+s);
		
	   m=new HashMap<String, String>();
		m.put("Content-Type", "text/html");

		}
		catch(Exception e)
		{		context.getLogger().log("landpage exp is: "+e);
		         throw e;
			
		}

	     return new ApiGatewayResponse(200,m,s,false);
	}
	
	private String createSession(LoginVO vo, Connection con) throws Exception{
		String f="";
		String sql="INSERT INTO user_session_tbl (user_email,session_token,expire_time,creation_time,is_logout)"
				+ " VALUES (?,?,?, now(),?)";
		
		PreparedStatement stmt=con.prepareStatement(sql);  
		 f=UUID.randomUUID().toString();
		stmt.setString(1,vo.getEmail());  
		
		stmt.setString(2,f); 
		stmt.setLong(3,System.currentTimeMillis()+15*60*1000);  
		stmt.setInt(4, 0);
		int i=stmt.executeUpdate();
		return f;
	}
}
