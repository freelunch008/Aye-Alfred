package com.ala.lam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.amazonaws.services.lambda.runtime.Context;

public class OauthCallBackEventHandler {

	public ApiGatewayResponse process(ApiGatewayRequest request, Context context) {
		// TODO Auto-generated method stub
		Connection con=null;
		try
		{
		con=DBConnection.getConnection();
		String authCode=request.getQueryStringParameters().get("code");
		UserSessionVO uvo=ProjHandler.isValidSession(request, con,0, context);
		String email=uvo.getRegisterEmail();
		AccessTokenVO vo=Oauth2Resource.getAccessTokenFromAuthCode(authCode);
		insertAceesToken(request,vo,email,context,con);
		String calenderEmail=Oauth2Resource.getLinkedEmailFromAccessToken(vo.getAccess_token());
		updateCalenderLink(request,calenderEmail, context,con);
		

		
		return new ProjHandler().process(request, context);
		}
		catch(Exception e)
		{
			context.getLogger().log("exception  OauthCallBackEventHandler: "+e);
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
		return new ProjHandler().process(request, context);
		
	}

	private void insertAceesToken(ApiGatewayRequest request, AccessTokenVO vo,String email, Context context, Connection con) throws Exception{
		String sql="INSERT INTO user_access_token_tbl (email,access_token,refresh_token)"
				+ " VALUES (?,?,?)";
		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,email);  
		stmt.setString(2,vo.getAccess_token());  
		stmt.setString(3,vo.getRefresh_token());  

		int i=stmt.executeUpdate();
		
	}

	private void updateCalenderLink(ApiGatewayRequest request, String calenderEmail,Context context,Connection con) throws Exception {
		String cookie=request.getHeaders().get("cookie");
		if(StringUtils.isEmpty(cookie))
			return;
		String  token=cookie.split("=")[1];
		
		if(StringUtils.isEmpty(token))
			return;
		
		String sql="UPDATE user_info_tbl SET is_calender_linked=1,linked_calender_email=? WHERE register_email="
				+ "(SELECT user_email FROM user_session_tbl WHERE session_token=?)";
		
		context.getLogger().log("header value for session token "+token);
		
		PreparedStatement stmt=con.prepareStatement(sql); 
		stmt.setString(1,calenderEmail);
		stmt.setString(2,token);  
		int i=stmt.executeUpdate();
		

	
		
	}

}
