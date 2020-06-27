package com.aws.service.demo.utils;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.s3.model.AmazonS3Exception;

public class ErrorHandler {
	
	 public static <T> void validating(T t) throws InputException {
	        Set<String> errors = new HashSet<>();
	        if (t == null) {
	            errors.add("Please pass valid input.");
	        } else {
	            Set<ConstraintViolation<T>> violations
	                    = Validation.buildDefaultValidatorFactory().getValidator().validate(t);
	            if (!violations.isEmpty()) {
	                for (ConstraintViolation<T> c : violations) {
	                    errors.add(c.getMessage());
	                }
	            }
	        }
	        if (!errors.isEmpty()) {
	            throw new InputException(errors);
	        }
	    }
	
	 public static void handleDynamo(Exception e, Response res) {
            if(e instanceof AmazonDynamoDBException || e instanceof SdkClientException) {
            	res.setMessage("Cannot perform action at the moment, try later");
            }
            else if(e instanceof InputException) {
            	InputException i = (InputException)e;
            	res.setMessage("Bad input" + " " + i.getErrors().toString());
            }
            else {
            	res.setMessage(e.getMessage());
            }
            res.setError(true);
	}
	 
	public static void handleS3(Exception e, Response res) {
		if(e instanceof AmazonS3Exception || e instanceof SdkClientException) {
        	res.setMessage("Cannot perform action at the moment, try later");
        }
		else if(e instanceof InputException) {
        	InputException i = (InputException)e;
        	res.setMessage("Bad input" + " " + i.getErrors().toString());
        }
        else {
        	res.setMessage(e.getMessage());
        }
        res.setStatuscode(500);
	}
}
