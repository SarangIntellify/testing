package com.amazonaws.lambda.demo;

public class User {

	private String name;
	private String state;
	private String email;
	private int id;
		
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", state=" + state + ", email=" + email + ", id=" + id + "]";
	}
	

	
	
}
