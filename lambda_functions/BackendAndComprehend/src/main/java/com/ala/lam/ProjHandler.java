package com.ala.lam;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

public class ProjHandler 
{

	public ApiGatewayResponse process(ApiGatewayRequest request,Context context) {
		Connection con=null;
		try
		{
		 con=DBConnection.getConnection();
		 UserSessionVO vo=isValidSession(request, con,0,context);
		if(vo!=null)
			return getHomePage(vo,context);
		else
			return getLandPage(vo,context);
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
	
	
	private ApiGatewayResponse getLandPage(UserSessionVO vo,Context context) throws Exception{
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


	private ApiGatewayResponse getHomePage(UserSessionVO vo,Context context) throws Exception {
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
		
		 context.getLogger().log("getIsCalenderLinked "+vo.getIsCalenderLinked());
		 
		 if(vo.getIsCalenderLinked()>0)
		 {
			 context.getLogger().log("userEmail "+vo.getRegisterEmail());
			 String r="<input type=\"hidden\" id=\"user-email\" value=\""+vo.getRegisterEmail()+"\">";
			 s.replace("<input type=\"hidden\" id=\"user-email\" value=\"\">", r);
			 
		 }
		 
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


	public static UserSessionVO isValidSession(ApiGatewayRequest request,Connection con,int isLogout,Context context) throws Exception
	{
	
		UserSessionVO vo=null;
		String cookie=request.getHeaders().get("cookie");
		if(StringUtils.isEmpty(cookie))
			return vo;
		String  token=cookie.split("=")[1];
		
		if(StringUtils.isEmpty(token))
			return vo;
		String sql=null;
		if(isLogout==0)
		 sql="SELECT ust.session_id,uit.name,uit.register_email,uit.is_calender_linked,uit.linked_calender_email,ust.expire_time"
				+ " FROM user_session_tbl ust JOIN user_info_tbl uit"
				+ " ON ust.user_email=uit.register_email WHERE ust.session_token=? AND ust.is_logout=?";
		else
			 sql="SELECT ust.session_id,uit.name,uit.register_email,uit.is_calender_linked,uit.linked_calender_email,ust.expire_time"
						+ " FROM user_session_tbl ust JOIN user_info_tbl uit"
						+ " ON ust.user_email=uit.register_email WHERE ust.session_token=?";
			
		
		context.getLogger().log("header value for session token "+token);
		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,token);
		if(isLogout==0)
		stmt.setInt(2, isLogout);
		ResultSet rs=null;
		rs=stmt.executeQuery();
		
		if(rs.next())
		{
			context.getLogger().log("header value for session token exists in DB ");
			long expire=rs.getLong("expire_time");
			
			if(System.currentTimeMillis()+2000<expire)
			{
				vo=new UserSessionVO();
				vo.setUserName(rs.getString("name"));
				vo.setRegisterEmail(rs.getString("register_email"));
				vo.setIsCalenderLinked(rs.getInt("is_calender_linked"));
				vo.setCalenderEmail(rs.getString("linked_calender_email"));
				context.getLogger().log(" session token is still valid ");
			}
			else
				context.getLogger().log(" session token is still valid ");
		}
		else
		{
			context.getLogger().log("header value for session token does not exist in DB ");
		}
	
		
		return vo;
	}
	

	public static String getCalendarEmailFromSessionID(String sid,Connection con,int isLogout,Context context) throws Exception
	{
	    String email="";
		String sql=null;
		if(isLogout==0)
		 sql="SELECT ust.session_id,uit.name,uit.register_email,uit.is_calender_linked,uit.linked_calender_email,ust.expire_time"
				+ " FROM user_session_tbl ust JOIN user_info_tbl uit"
				+ " ON ust.user_email=uit.register_email WHERE ust.session_token=? AND ust.is_logout=?";
		else
			 sql="SELECT ust.session_id,uit.name,uit.register_email,uit.is_calender_linked,uit.linked_calender_email,ust.expire_time"
						+ " FROM user_session_tbl ust JOIN user_info_tbl uit"
						+ " ON ust.user_email=uit.register_email WHERE ust.session_token=?";
			
		
		context.getLogger().log("session token "+sid);
		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,sid);
		if(isLogout==0)
		stmt.setInt(2, isLogout);
		ResultSet rs=null;
		rs=stmt.executeQuery();
		
		if(rs.next())
		{
			context.getLogger().log("header value for session token exists in DB ");
			long expire=rs.getLong("expire_time");
			

				email=rs.getString("linked_calender_email");

		}
		else
		{
			context.getLogger().log("header value for session token does not exist in DB ");
		}
	
		
		return email;
	}

}
