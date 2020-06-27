package com.aws.service.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.service.demo.dto.User;
import com.aws.service.demo.services.AWSDynamoservice;
import com.aws.service.demo.utils.Response;

@RestController
@RequestMapping("/dynamo")
public class AWSDynamocontroller {

	@Autowired
	private AWSDynamoservice dservice;

	@PostMapping("/add")
	public Response addItem(@RequestBody User user) {
		return dservice.addItem(user);
	}

	@GetMapping("/getinfo/{id}")
	public Response getItem(@PathVariable("id") String id) {
		return dservice.getItem(id);
	}

	@DeleteMapping("/deleteinfo/{id}")
	public Response deleteItem(@PathVariable("id") String id) {
		return dservice.deleteItem(id);
	}
}
