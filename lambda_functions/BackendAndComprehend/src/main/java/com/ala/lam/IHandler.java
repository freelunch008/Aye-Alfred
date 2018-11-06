package com.ala.lam;

import com.amazonaws.services.lambda.runtime.Context;

public interface IHandler {

	public ApiGatewayResponse processRequest(ApiGatewayRequest request,Context context);
}
