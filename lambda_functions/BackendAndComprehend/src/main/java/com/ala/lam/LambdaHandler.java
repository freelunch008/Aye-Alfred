package com.ala.lam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {
	


	public ApiGatewayResponse handleRequest(ApiGatewayRequest request, Context context) {
		// TODO Auto-generated method stub
		LambdaLogger logger=context.getLogger();
		logger.log("First entry into lambda  "+context.toString());
		
		logger.log("db url is "+System.getenv("DB_URL"));
		logger.log("*********************************");
		
		logger.log("Path is "+request.getPath());
		logger.log("Method is "+request.getHttpMethod());
		logger.log("Request body is "+request.getBody());
		
		Map<String,String> headers=request.getHeaders();
		int i=1;
		if(headers!=null)
		{
			if(!headers.isEmpty())
			for (Map.Entry<String, String> entry : headers.entrySet())
			{   logger.log("header No "+i);
				logger.log(entry.getKey() + "*****" + entry.getValue());
				i++;
			}
			else
				logger.log(" header map is empty");
		}
		else
			 logger.log("no header present in the request");
		
		
		Map<String,String> queryParams=request.getQueryStringParameters();
		 i=1;
		if(queryParams!=null)
		for (Map.Entry<String, String> entry : queryParams.entrySet())
		{   logger.log("query Param No "+i);
			logger.log(entry.getKey() + "/" + entry.getValue());
			i++;
		}
		
		
		Map<String,String> stage=request.getStageVariables();
		i=1;
		if(stage!=null)
		for (Map.Entry<String, String> entry : stage.entrySet())
		{   logger.log("stage "+i);
			logger.log(entry.getKey() + "/" + entry.getValue());
			i++;
		}
		
		String x="{\n\"code\":\"0\",\n \"calender_linked\":0,\n \"link_url\":\"https://accounts.google.com/o/oauth2/auth?redirect_uri=https://kt2n8fjew9.execute-api.us-east-2.amazonaws.com/dev/oauth2callback&response_type=code&client_id=208971152413-0fqu23rsim920do7tbklb99t254h92sk.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/calendar+https://www.googleapis.com/auth/userinfo.email&approval_prompt=force&access_type=offline\"\n}";
		
		
		if(request.getPath().equals("/calpro"))
		{
			logger.log("entered into login calpro ***");
			Map<String,String> m=new HashMap<String, String>();
			m.put("Content-Type", "text/html");
			String v="<html><body><p>Hi welcome to my webpage</p></body></html>";
			//return new ApiGatewayResponse(200,null,x,false);
			
			return new ProjHandler().process(request,context);
			
		}
		
		if(request.getPath().equals("/createEvent"))
		{
			logger.log("entered into login createEvent ***");
			//String v="<html><body><p>Hi welcome to my webpage</p></body></html>";
			//return new ApiGatewayResponse(200,m,v,false);
			
			return new CreateEventHandler().process(request, context);
			
		}
		
		if(request.getPath().equals("/meetingRequest"))
		{
			logger.log("entered into login meetingRequest ***");
			//String v="<html><body><p>Hi welcome to my webpage</p></body></html>";
			//return new ApiGatewayResponse(200,m,v,false);
			
			return new MeetingRequestHandler().process(request, context);
			
		}
		
		
		if(request.getPath().equals("/login"))
		{
			logger.log("entered into login path ***");
			Map<String,String> m=new HashMap<String, String>();
			m.put("Content-Type", "text/html");
			String v="<html><body><p>Hi welcome to my webpage</p></body></html>";
			//return new ApiGatewayResponse(200,m,v,false);
			return new LoginHandler().process(request, context);
			
		}
		
		if(request.getPath().equals("/register"))
		{
			logger.log("entered into register path ***");
			Map<String,String> m=new HashMap<String, String>();
			m.put("Content-Type", "text/html");
			String v="<html><body><p>Hi welcome to my webpage</p></body></html>";
			//return new ApiGatewayResponse(200,m,v,false);
			return new SignUpHandler().process(request, context);
			
		}
		
		
		
		String y="{\n\"code\":\"0\",\n \"calender_linked\":1,\n \"link_url\":\"https://accounts.google.com/o/oauth2/auth?redirect_uri=https://kt2n8fjew9.execute-api.us-east-2.amazonaws.com/dev/oauth2callback&response_type=code&client_id=208971152413-0fqu23rsim920do7tbklb99t254h92sk.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/calendar+https://www.googleapis.com/auth/userinfo.email&approval_prompt=force&access_type=offline\"\n}";

		if(request.getPath().equals("/oauth2callback"))
		{
			logger.log("entered into oauth2callback path ***");
			//return new ApiGatewayResponse(200,null,y,false);
			
			return new OauthCallBackEventHandler().process(request, context);
			
		}
		
		String handlerType=getHandlerType(request.getPath(), request.getHttpMethod());
		
		logger.log("handler type is "+handlerType);
		
		if(handlerType.equals("invalid"))
			return new ApiGatewayResponse(405,null,"bad request",false);
		
		  IHandler handler= HandlersFactory.getHandlersMap().get(handlerType);
		  if(handler!=null)
		  handler.processRequest(request,context);
		  else
			  return new ApiGatewayResponse(405,null,"bad request",false);
			
		
		ApiGatewayResponse response=new ApiGatewayResponse(200,null,"ok custom response",false);
		
		
		return response;
	}
	
	public String getHandlerType(String path, String method)
	{
		path=path.substring(1);
		if(path.endsWith("/"))
			path=path.substring(0, path.length() - 1);
		
		String [] paths=path.split("/");
          
		if(paths.length>2 || paths.length<1)
			return "invalid";
		else if(paths.length==1)
		{
			if(paths[0].equals("notificationhosts")&& method.equals("POST"))
				return "CREATE_HOST";
		    if(paths[0].equals("template")&&method.equals("POST"))
				return "NOTIFICATION";
			
				return "invalid";
		}
		
		else if(paths.length==2)
		{
			if(paths[0].equals("notificationhosts"))
			{
				if(isLong(paths[1]))
						{
				if(method.equals("PUT"))
					return "UPDATE_HOST";
				
				if(method.equals("GET"))
					return "GET_HOST";
				
				if(method.equals("DELETE"))
					return "DELETE_HOST";
						}
				else
					return "invalid";
			}
				
			
			
			if(paths[0].equals("notificationhosts"))
				return "NOTIFICATION";
			
		}
	
		
		return "invalid";
	}
	
	
	public static boolean isLong(String str) 
	{
		try  
		  {  
		    long d =Long.parseLong(str);
		  }  
		  catch(Exception nfe)  
		  {  
		    return false;  
		  }  
		  return true; 
	}


}
