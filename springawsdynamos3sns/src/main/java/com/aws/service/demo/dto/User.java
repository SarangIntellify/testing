package com.aws.service.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

	private String id;
	
	@NotNull(message = "Enter valid name(3-100)")
    @NotEmpty(message = "Enter valid name(3-100)")
    @Size(min = 3, max = 127, message = "Enter valid name(3-100)")
    @JsonProperty("name")
	private String name;
	
	@Email
	@JsonProperty("email")
	private String email;
	
	@NotNull(message = "Enter valid contact number(10)")
    @NotEmpty(message = "Enter valid contact number(10)")
    @Size(min = 10, max = 10, message = "Enter valid contact number(10)")
    @JsonProperty("contact")
	private String contact;
	
	private String creation_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(String creation_time) {
		this.creation_time = creation_time;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", name=" + name + ", email=" + email + ", contact=" + contact + ", creation_time="
				+ creation_time + "}";
	}
	
	
	
}
