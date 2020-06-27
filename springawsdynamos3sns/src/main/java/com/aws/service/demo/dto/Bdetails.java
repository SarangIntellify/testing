package com.aws.service.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bdetails {
	
	@NotNull(message = "Enter valid bucket name(5-20)")
    @NotEmpty(message = "Enter valid bucket name(5-20)")
    @Size(min = 5, max = 20, message = "Enter valid bucket name(5-20)")
    @JsonProperty("bname")
	private String bname;
	
	@NotNull(message = "Enter valid name(5-20)")
    @NotEmpty(message = "Enter valid name(5-20)")
    @Size(min = 5, max = 20, message = "Enter valid name(5-20)")
    @JsonProperty("key")
	private String key;

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "Bdetails [bname=" + bname + ", key=" + key + "]";
	}
	
	
}
