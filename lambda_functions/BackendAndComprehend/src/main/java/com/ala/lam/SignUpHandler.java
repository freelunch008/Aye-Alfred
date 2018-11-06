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

public class SignUpHandler {

	
	public ApiGatewayResponse process(ApiGatewayRequest request,Context context) {
		Connection con=null;
		boolean register=false;
		String response="";
		try
		{
			
	    String body=request.getBody();
	    Gson gson = new Gson(); // Or use new GsonBuilder().create();
	    SignUpVO vo = gson.fromJson(body, SignUpVO.class);
			
		 con=DBConnection.getConnection();
		 boolean duplicate=checkDuplicity(vo, con);
		 context.getLogger().log("Is Duplicate user register "+duplicate);
		 if(!duplicate)
		 {
		
			 register=registerUser(vo, con);
		 }
		 context.getLogger().log("Register done "+register);
		 if(register)
			 response="{\"code\":\"0\",\"msg\":\"success\"}";
		 else
			 response="{\"code\":\"500\",\"msg\":\"fail\"}";
		 
			Map<String,String> m=new HashMap<String, String>();
			m.put("Content-Type", "application/json");
			String v="<html><body><p>Hi welcome to my webpage</p></body></html>";
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
	
	private boolean checkDuplicity(SignUpVO vo, Connection con) throws Exception{
		boolean f=true;
		String sql="SELECT user_id FROM user_info_tbl WHERE register_email=? OR fname=? OR lname=?";
		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,vo.getEmail().trim());  
		stmt.setString(2,vo.getFisrtName().trim()); 
		stmt.setString(3,vo.getLastName().trim()); 

		ResultSet rs=stmt.executeQuery();
		if(!rs.next())
			 f=false;
		return f;
	}



	private boolean registerUser(SignUpVO vo, Connection con) throws Exception{
		boolean f=false;
		String sql="INSERT INTO user_info_tbl (name,register_email,is_calender_linked,password,creation_time)"
				+ " VALUES (?,?,?,?, now())";
		
		PreparedStatement stmt=con.prepareStatement(sql);  
		stmt.setString(1,vo.getFisrtName().trim()+" "+vo.getLastName().trim());  
		stmt.setString(2,vo.getEmail());  
		stmt.setInt(3,0);  
		stmt.setString(4, vo.getPassword());
		int i=stmt.executeUpdate();
		f=true;
		
	
	
		
		return f;
	}


}
