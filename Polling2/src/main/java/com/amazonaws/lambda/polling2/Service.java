package com.amazonaws.lambda.polling2;

import java.io.IOException;
import java.net.URLEncoder;

import com.amazonaws.services.kms.model.InvalidArnException;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClient;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.ActivityDoesNotExistException;
import com.amazonaws.services.stepfunctions.model.ActivityWorkerLimitExceededException;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskRequest;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import com.amazonaws.services.stepfunctions.model.SendTaskSuccessRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Service {

	private AWSStepFunctionsClient stepclient = (AWSStepFunctionsClient) AWSStepFunctionsClientBuilder.standard().withRegion("us-west-2").build();
	private EmailService eservice = new EmailService();	

	@SuppressWarnings("deprecation")
	public String polling2() {
		StringBuilder builder = new StringBuilder();
		try {
		 GetActivityTaskRequest request = new GetActivityTaskRequest();
		 request.setActivityArn("arn:aws:states:us-west-2:987450053827:activity:ManualInstitutionalActivity");
		 request.setWorkerName("Sarang Kumar");
		 GetActivityTaskResult result = stepclient.getActivityTask(request);
		 String tasktoken = result.getTaskToken();
		 String inpur = result.getInput();
		 SendTaskSuccessRequest s = new SendTaskSuccessRequest();
		 builder.append("Received tasktokens and inputs").append('\n');
		 
		 //reading input from activity		 
		 ObjectMapper map = new ObjectMapper();
		 FromStep fromstep = map.readValue(inpur, FromStep.class);
		 String email = fromstep.getEmail();
		 String type = fromstep.getType();
	     
		 builder.append(email).append('\n');
		 builder.append(type).append('\n');
		 
		 //sending mail to client	 
		boolean ans = eservice.mail("sarang.t@theintellify.com",email,"Your Approval Needed!",new StringBuilder().append("Can you please approve : https://ktsath730c.execute-api.us-west-2.amazonaws.com/pro/approve?taskToken=" + URLEncoder.encode(tasktoken)).append('\n').append("Reject : https://ktsath730c.execute-api.us-west-2.amazonaws.com/pro/reject?taskToken=" + URLEncoder.encode(tasktoken)).toString());
		
		if(ans == true) {builder.append("Email Sent").append('\n');}
		else {builder.append("Email not Sent").append('\n');}
				 
		}
		 catch(ActivityDoesNotExistException e) {
			 builder.append("ActivityDoesNotExistException").append('\n');
		 }
		 catch(ActivityWorkerLimitExceededException e) {
			 builder.append("ActivityWorkerLimitExceededException").append('\n');
		 }
		 catch(InvalidArnException e) {
			 builder.append("InvalidArnException").append('\n');
		 }
		 catch(JsonMappingException e) {
			 builder.append("JsonMappingException").append('\n');
			 
		 } catch (JsonParseException e) {
			 builder.append("JsonParseException").append('\n');
		
		} catch (IOException e) {
			 builder.append("IOException").append('\n');
		}
		
		return builder.toString();
	}
	
	
	
}
