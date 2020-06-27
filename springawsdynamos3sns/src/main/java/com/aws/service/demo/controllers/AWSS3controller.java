package com.aws.service.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aws.service.demo.dto.Bdetails;
import com.aws.service.demo.services.AWSS3service;
import com.aws.service.demo.utils.Response;

@RestController
@RequestMapping("/admin/s3")
public class AWSS3controller {

	@Autowired
	private AWSS3service service;

	@PostMapping("/createbucket/{bname}")
	public Response createBucket(@PathVariable("bname") String bname) {
		return service.createBucket(bname);
	}

	@GetMapping("/getobject")
	public Response getObject(@RequestParam String bname, String key) {
		return service.getContent(bname, key);
	}

	@GetMapping("/listbuckets")
	public Response listBucket() {
		return service.listBucket();
	}

	@PostMapping("/write")
	public Response write(@RequestBody Bdetails details) {
		return service.write(details);
	}
	
	@PostMapping("/createfolder")
	public String createFolder(@RequestParam String bname, String fname) {
		return service.createFolder(bname,fname);
	}

}
