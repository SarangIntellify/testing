package com.amazonaws.lambda.system;

import java.util.Map;

public class Response {
    
	private boolean isBase64Encoded;
	private int statusCode;
	private String body;
	private Map<String, String> headers;
	
	public boolean isBase64Encoded() {
		return isBase64Encoded;
	}
	public void setBase64Encoded(boolean isBase64Encoded) {
		this.isBase64Encoded = isBase64Encoded;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	
	
	
	
	
}
