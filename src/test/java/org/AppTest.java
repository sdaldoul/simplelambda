package org;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import org.example.NewOffer;
import org.example.OfferState;
import org.junit.Test;
import org.mockito.Mockito;

public class AppTest {

	private static final String FILENAME = "inliDoc.html";

	@Test
	public void checkEmailStateExists() {

		Context context = Mockito.mock(Context.class);
		LambdaLogger lambdaLogger = Mockito.mock(LambdaLogger.class);

		Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
		OfferState offerState = new OfferState(context);

		Set<String> apartmentsFound = new HashSet<>();
		apartmentsFound.add("/paris/PRV-346744");
		apartmentsFound.add("/antony/PRV-346731");
		apartmentsFound.add("/antony/PRV-346726");
		apartmentsFound.add("/nanterre/PRV-346733");
		apartmentsFound.add("/reuil/PRV-346744");

		String actual = "[/antony/PRV-346731, /antony/PRV-346726, /antony/PRV-346727]";

		System.out.println("apartmentsFound " + apartmentsFound);
		System.out.println("actual " + actual);

		Set<String> emailSet = offerState.getEmailState(apartmentsFound, actual);

		System.out.println("emailSet " + emailSet);
		assertEquals(emailSet.toString(), "[/nanterre/PRV-346733, /paris/PRV-346744, /reuil/PRV-346744]");

	}

	@Test
	public void checkEmailFirstRunFound() {

		Context context = Mockito.mock(Context.class);
		LambdaLogger lambdaLogger = Mockito.mock(LambdaLogger.class);

		Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
		OfferState offerState = new OfferState(context);

		Set<String> apartmentsFound = new HashSet<>();
		apartmentsFound.add("/paris/PRV-346744");
		apartmentsFound.add("/antony/PRV-346731");
		apartmentsFound.add("/antony/PRV-346726");
		apartmentsFound.add("/nanterre/PRV-346733");
		apartmentsFound.add("/reuil/PRV-346744");

		String actual = "";

		System.out.println("apartmentsFound " + apartmentsFound);
		System.out.println("actual " + actual);

		Set<String> emailSet = offerState.getEmailState(apartmentsFound, actual);

		System.out.println("emailSet " + emailSet);
		assertEquals(emailSet.toString(),
				"[/nanterre/PRV-346733, /antony/PRV-346731, /paris/PRV-346744, /antony/PRV-346726, /reuil/PRV-346744]");

	}


	@Test
	public void checkEmailFirstRunEmpty() {

		Context context = Mockito.mock(Context.class);
		LambdaLogger lambdaLogger = Mockito.mock(LambdaLogger.class);

		Mockito.when(context.getLogger()).thenReturn(lambdaLogger);
		OfferState offerState = new OfferState(context);

		Set<String> apartmentsFound = new HashSet<>();
		String actual = "";

		System.out.println("apartmentsFound " + apartmentsFound);
		System.out.println("actual " + actual);

		Set<String> emailSet = offerState.getEmailState(apartmentsFound, actual);

		System.out.println("emailSet " + emailSet);
		assertEquals(emailSet.toString(), "[]");

	}

	@Test
	public void checkDoc() throws Exception {

		Path resourceLocation = Paths.get(this.getClass().getResource("/").toURI()).resolve(FILENAME);

		NewOffer app = new NewOffer();

		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(resourceLocation.toFile()));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		Set<String> apartmentsFound = app.findAllApartement(content);
		assertNotNull(apartmentsFound);
		assertTrue(apartmentsFound.size() == 4);
		System.out.println(apartmentsFound);
	}

}
