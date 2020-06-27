package com.aws.service.demo.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.service.demo.dto.Bdetails;
import com.aws.service.demo.utils.ErrorHandler;
import com.aws.service.demo.utils.Response;

@Service
public class AWSS3service {
  
	private Logger log = LoggerFactory.getLogger(AWSS3service.class);
	
	@Autowired
	private Response res;
	@Autowired
	private Environment env;

	private AmazonS3 client;

	@PostConstruct
	public void init() {
		client = AmazonS3ClientBuilder.standard().withRegion(env.getProperty("region")).build();
	}

	public Response createBucket(String bname) {
		try {
			if (!client.doesBucketExistV2(bname)) {
				client.createBucket(bname);
				res.setMessage(bname + " " + "Bucket created Successfully");
			} else {
				res.setMessage(bname + " " + "already exists");
			}
		} catch (Exception e) {
			log.error("error",e);
			ErrorHandler.handleS3(e, res);
		}
		res.setStatuscode(res.isError() == true ? 500 : 200);
		return res;
	}
	
	public Response listBucket() {
		try {
			List<Bucket> list = client.listBuckets();
			if (!list.isEmpty() && list != null) {
				res.setMessage("List of Buckets" + " " + list.toString());
			} else {
				res.setMessage("No Buckets exists");
			}
		} catch (Exception e) {
			log.error("error",e);
			ErrorHandler.handleS3(e, res);
		}
		res.setStatuscode(res.isError() == true ? 500 : 200);
		return res;
	}

	public Response getContent(String bname, String key) {
		try {
			S3Object obj = client.getObject(bname, key);
			handleStream(obj.getObjectContent());
		} catch (Exception e) {
			log.error("error",e);
			ErrorHandler.handleS3(e, res);
		}
		res.setStatuscode(res.isError() == true ? 500 : 200);
		return res;
	}

	public void handleStream(InputStream input) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line).append('\n');
			}
			res.setMessage("Object Content" + " " + builder.toString());
		} catch (IOException e) {
			log.error("error",e);
			res.setMessage(e.getMessage());
		}
	}

	public Response write(Bdetails details) {
		try {
			ErrorHandler.validating(details);
			String bname = details.getBname();
			String key = details.getKey();
			createBucket(bname);
			File f = new File("input");
			client.putObject(bname, key, f);
			res.setMessage("Object added to bucket");
		} catch (Exception e) {
			log.error("error",e);
			ErrorHandler.handleS3(e, res);
		}
		res.setStatuscode(res.isError() == true ? 500 : 200);
		return res;
	}
	
	 public String createFolder(String bucketName, String folderName) throws AmazonServiceException {
	        
		   // create meta-data for your folder and set content-length to 0
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentLength(0);

	        // crete empty content
	        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

	        // create a PutObjectRequest passing the folder name suffixed by /
	        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
	                folderName + "/", emptyContent, metadata);

	        // send request to S3 to create folder
	        client.putObject(putObjectRequest);
	        return "200";
	    }

}
