package com.aws.service.demo.services;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

@Service
public class AWSSNSservice {

	@Autowired
	private Environment env;
	
	private AmazonSNSClient client;
	
	@PostConstruct
	public void init() {
		client = (AmazonSNSClient)AmazonSNSClientBuilder.standard().build();
	}
		
	public boolean publishMessage(String key, Item item) {
		String subject= "Detail request for user_id = " + key;
		String message = "User Details = " + item.toString();
		if(Objects.nonNull(client.publish(env.getProperty("topicarn"), message, subject)))return true;
		else return false;
	}
	
}
