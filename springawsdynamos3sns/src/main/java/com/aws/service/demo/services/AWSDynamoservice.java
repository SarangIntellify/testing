package com.aws.service.demo.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.aws.service.demo.dto.User;
import com.aws.service.demo.utils.ErrorHandler;
import com.aws.service.demo.utils.Response;

@Service
public class AWSDynamoservice {
   
	private Logger log = LoggerFactory.getLogger(AWSDynamoservice.class);
	
	@Autowired
	private AWSSNSservice sns;
	@Autowired
	private Environment env;
	@Autowired
	private Response res;

	private AmazonDynamoDB client;
	private DynamoDB clientdb;

	@PostConstruct
	public void init() {
		client = AmazonDynamoDBClientBuilder.standard().withRegion(env.getProperty("region")).build();
		clientdb = new DynamoDB(client);
	}

	public Response addItem(User user) {
		String newLine = System.getProperty("line.separator");
		try {
			ErrorHandler.validating(user);
			user.setId(UUID.randomUUID().toString());
			user.setCreation_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			Table table = clientdb.getTable(env.getProperty("tablename"));
			Item item = new Item();
			item.withPrimaryKey("user_id", user.getId()).with("name", user.getName()).with("email", user.getEmail())
					.with("contact", user.getContact()).with("creation_time", user.getCreation_time());
           try {
			File f = new File("input");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(user.toString() + newLine);
			bw.flush();
			bw.close();
           }
           catch(IOException i) {
        	   log.error("error",i);
           }

			if (Objects.nonNull(table.putItem(item))) {
				res.setMessage("Item added Successfully");
			} else {
				res.setMessage("Cannot add item at the moment,try again later");
			}
		} catch (Exception e) {
			log.error("error",e);
			ErrorHandler.handleDynamo(e, res);
		}
		res.setStatuscode(res.isError() == true ? 500 : 200);
		return res;
	}

	public Response getItem(String id) {
		try {
			Table table = clientdb.getTable(env.getProperty("tablename"));
			GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_id", id);
			Item item = table.getItem(spec);
			res.setMessage(item.toString());
		} catch (Exception e) {
			log.error("error",e);
			ErrorHandler.handleDynamo(e, res);
		}
		res.setStatuscode(res.isError() == true ? 500 : 200);
		return res;
	}

	public Response deleteItem(String id) {
		try {
			Table table = clientdb.getTable(env.getProperty("tablename"));
			GetItemSpec spec = new GetItemSpec().withPrimaryKey("user_id", id);
			Item item = table.getItem(spec);
			if (Objects.nonNull(item)) {
				if (sns.publishMessage(id, item)) {
					res.setMessage("Delete request has been accepted, check mail for confirmation");
				} else {
					res.setMessage("Try again later, please");
				}
			}
		} catch (Exception e) {
			log.error("error",e);
			ErrorHandler.handleDynamo(e, res);
		}
		res.setStatuscode(res.isError() == true ? 500 : 200);
		return res;
	}

}
