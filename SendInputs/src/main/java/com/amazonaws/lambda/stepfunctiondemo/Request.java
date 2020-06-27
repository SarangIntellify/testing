package com.amazonaws.lambda.stepfunctiondemo;

public class Request {
   
	private String body;
	private String eventname;
	private String statemachinearn;
	
	public String getStatemachinearn() {
		return statemachinearn;
	}

	public void setStatemachinearn(String statemachinearn) {
		this.statemachinearn = statemachinearn;
	}

	public String getEventname() {
		return eventname;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
