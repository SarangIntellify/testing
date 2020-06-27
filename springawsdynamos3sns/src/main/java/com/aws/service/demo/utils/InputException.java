package com.aws.service.demo.utils;

import java.util.Set;

public class InputException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public Set<String> errors;
	
	public InputException(Set<String> errors) {
		super(errors.toString());
		this.errors = errors;
	}

	public Set<String> getErrors() {
		return errors;
	}

	public void setErrors(Set<String> errors) {
		this.errors = errors;
	}
	

}
