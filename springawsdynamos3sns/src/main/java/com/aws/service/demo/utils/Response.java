package com.aws.service.demo.utils;

import org.springframework.stereotype.Component;

@Component
public class Response {
	
	private String message;
	private int statuscode;
	private boolean error;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	@Override
	public String toString() {
		return "Response [message=" + message + ", statuscode=" + statuscode + ", error=" + error + "]";
	}
	
	
}
