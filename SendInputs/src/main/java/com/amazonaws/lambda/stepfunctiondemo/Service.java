package com.amazonaws.lambda.stepfunctiondemo;

import com.amazonaws.services.stepfunctions.AWSStepFunctionsClient;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;

import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;

public class Service {
	
 private AWSStepFunctionsClient stepclient = (AWSStepFunctionsClient) AWSStepFunctionsClientBuilder.standard().withRegion("us-west-2").build();
 //private AWSLambdaClient lclient = (AWSLambdaClient)AWSLambdaClientBuilder.standard().withRegion("us-west-2").build();;

 public String process(Request input) {
     StringBuilder builder = new StringBuilder();

	 StartExecutionRequest ser = new StartExecutionRequest();
	 ser.setStateMachineArn(input.getStatemachinearn());
	 ser.setName(input.getEventname());
	 ser.setInput(input.getBody());
	 
	 stepclient.startExecution(ser);
	 builder.append("input sent");
		 
		/*
		 * InvokeRequest ir = new InvokeRequest();
		 * ir.setFunctionName(input.getLambdaarn());
		 * ir.setInvocationType(input.getInvocationtype()); lclient.invoke(ir);
		 * builder.append("Mail sent");
		 */
	 
	 return builder.toString();
	}
	
}
