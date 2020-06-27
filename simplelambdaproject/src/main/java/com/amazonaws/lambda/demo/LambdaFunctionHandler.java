package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Request, Response> {

    @Override
    public Response handleRequest(Request input, Context context) {
          String method = input.getHttpmethod();
          if(method.equalsIgnoreCase("POST")) {
        	  Database d = new Database();
        	  return d.post(input);
           }
          else if(method.equalsIgnoreCase("GET")) {
        	  Database d = new Database();
        	  return d.get(input);
          }
          return null;
         
    }

}
