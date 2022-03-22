package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Set;

/**
 * Handler for requests to Lambda function.
 */
public class SimpleLambda implements RequestHandler<Void, String> {

	private final NewOffer newOffer = new NewOffer();
	private final OfferState offerState = new OfferState();
	private final AmazonSESSample amazonSESSample = new AmazonSESSample();

	public String handleRequest(final Void input, Context context) {

		try {

			offerState.setLogger(context.getLogger());
			amazonSESSample.setLogger(context.getLogger());

			// get new offers
			Set<String> apartmentsFound = this.newOffer.getApartmentsFound();
			context.getLogger().log("findAllApartment : " + apartmentsFound + "\n");

			// check existing offers
			String actual = this.offerState.getActualState();

			// extract new offers compared with old offers
			Set<String> emailSet = offerState.getEmailState(apartmentsFound, actual);
			if (emailSet.size() > 1) {
				context.getLogger().log("emailSet.size() > 1 : " + emailSet.size() + "\n");
				// save new list found
				offerState.saveState(emailSet);
				// send email
				this.amazonSESSample.sendEmail(emailSet);
			} else {
				context.getLogger().log("emailSet.size() = 0 : " + emailSet.size() + "\n");
				context.getLogger().log("No email has been sent" + "\n");
			}
			return String.valueOf(emailSet);
		} catch (Exception e) {
			String exceptionMsg = "Exception :  " + e.getMessage();
			context.getLogger().log(exceptionMsg + "\n");
			return exceptionMsg;
		}
	}
}
