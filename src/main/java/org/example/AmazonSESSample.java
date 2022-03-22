package org.example;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.time.LocalDateTime;
import java.util.Set;

public class AmazonSESSample {

	private LambdaLogger logger;

	public void setLogger(LambdaLogger logger) {
		this.logger = logger;
	}

	public AmazonSESSample(){
	}
	public AmazonSESSample(Context context){
		this.logger = context.getLogger();
	}

	static  final String INLIOFFERLINK = "https://www.inli.fr/locations/offre";

	// Replace sender@example.com with your "From" address.
	// This address must be verified with Amazon SES.
	static final String FROM = System.getenv("FROM_EMAIL");

	// Replace recipient@example.com with a "To" address. If your account
	// is still in the sandbox, this address must be verified.
	static final String TO = System.getenv("TO_EMAIL");


	// The subject line for the email.
	static final String SUBJECT = "NEW OFFERS FROM INLI - DATE -  "+ LocalDateTime.now();

	public  void sendEmail(Set<String> apartmentsFound) {

			StringBuilder contentBodyUrlBuilder = new StringBuilder();

			apartmentsFound.forEach(s -> {
				contentBodyUrlBuilder.append(INLIOFFERLINK).append(s).append("\n");
			});

			AmazonSimpleEmailService client =
					AmazonSimpleEmailServiceClientBuilder.standard()
							.withRegion(Regions.EU_WEST_3).build();
			SendEmailRequest request = new SendEmailRequest()
					.withDestination(
							new Destination().withToAddresses(TO))
					.withMessage(new Message()
							.withBody(new Body()
									.withText(new Content()
											.withCharset("UTF-8").withData(contentBodyUrlBuilder.toString())))
							.withSubject(new Content()
									.withCharset("UTF-8").withData(SUBJECT)))
					.withSource(FROM);
			client.sendEmail(request);
		logger.log("Email sent! \n");

	}
}
