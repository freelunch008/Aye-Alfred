package com.ala.lam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;

public class MeetingRequestHandler {

	public ApiGatewayResponse process(ApiGatewayRequest request, Context context) 
	{

		Connection con=null;
		String success="{\"code\":\"0\",\"msg\":\"success\"}";
		String error="{\"code\":\"500\",\"msg\":\"error\"}";
		Map<String,String> m=new HashMap<String, String>();
		m.put("Content-Type", "application/json");
		try
		{
		con=DBConnection.getConnection();
		 UserSessionVO vo= ProjHandler.isValidSession(request, con,1,context);
		if(vo==null)
			return new ApiGatewayResponse(200,m,error,false);
		else
		{	
		
		context.getLogger().log("entered into MeetingRequestHandler-req body "+request.getBody());
	    Gson gson = new Gson();
		String reqBody=request.getBody();
	    MeetingRequestVO mvo= gson.fromJson(reqBody,MeetingRequestVO.class);
	    pushToQueue(con,reqBody,context);
		}
		
		}
		catch(Exception e)
		{
			context.getLogger().log("exception in MeetingRequestHandler "+e);
			return new ApiGatewayResponse(200,m,error,false);
		}
		return new ApiGatewayResponse(200,m,success,false);
	}

	private void pushToQueue(Connection con, String req, Context context)
	{
		AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
		sqs.listQueues();
		String qurl="https://sqs.us-east-2.amazonaws.com/567064759545/MeetingQ";
		
        sqs.sendMessage(new SendMessageRequest(qurl,req));
        
        context.getLogger().log("successfully pushed to SQS");
		
	}
	


}
