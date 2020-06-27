package com.amazonaws.lambda.demo;

public class Response {
   
	private int statuscode;
	private User user;
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Response [statuscode=" + statuscode + ", user=" + user + "]";
	}
	
	
	
}
