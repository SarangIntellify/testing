package com.amazonaws.lambda.system;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class EmailService {

	private final Logger log = LoggerFactory.getLogger(EmailService.class);
	public final AmazonSimpleEmailService client = AmazonSimpleEmailServiceAsyncClientBuilder.standard()
			                                 .withRegion("us-west-2").build();
	
	
	public boolean mail(String from, String to, String Subject, String body) {
		try {
		SendEmailRequest req = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(to))
				.withMessage(new Message()
				.withBody(new Body()
				.withText(new Content()
					.withCharset("UTF-8").withData(body)))
				 .withSubject(new Content()
					.withCharset("UTF-8").withData(Subject)))
				  .withSource(from);
		if(Objects.nonNull(client.sendEmail(req))) return true; }
		catch(Exception e) {
			log.error("",e);
		}
		return false;
}
	
	
}
