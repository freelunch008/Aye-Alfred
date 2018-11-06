package com.ala.lam;

import java.io.IOException;

import com.ala.lam.vo.Host;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateHostHandler implements IHandler{

	LambdaLogger logger=null;
	public ApiGatewayResponse processRequest(ApiGatewayRequest request, Context context) {
	  logger=context.getLogger();
		
		logger.log("inside CreateHostHandler()");
		try
		{
		Host host=getHostFromRequestBody(request.getBody());
		}
		catch(Exception e)
		{
			logger.log("exeption is "+e);
		}
		return null;
	}

	
	public Host getHostFromRequestBody(String body) throws JsonParseException, JsonMappingException, IOException 
	{
		logger.log("req body "+body);
		ObjectMapper mapper = new ObjectMapper();
		
		Host host=mapper.readValue(body, Host.class);
		
		return host;
		
	}
	

}
