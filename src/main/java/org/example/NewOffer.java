package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NewOffer {

	public static final String INLI_URL = "INLI_URL";

	private LambdaLogger logger;

	public NewOffer() {
	}

	public NewOffer(Context context) {
		this.logger = context.getLogger();
	}


	public Set<String>  getApartmentsFound() throws IOException {
		String inliUrl = System.getenv(INLI_URL);
		final String pageContentsInli = this.getPageContents(inliUrl);
		return findAllApartement(pageContentsInli);
	}

	private String getPageContents(String address) throws IOException {
		URL url = new URL(address);
		URLConnection uc = url.openConnection();
		uc.setReadTimeout(4000);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()))) {
			return br.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}


	public Set<String> findAllApartement(String content) {
		Set<String> apartmentsFound = new HashSet<>();
		String start = "<a href=\"/locations/offre";
		String end = "\">";
		Pattern pattern = Pattern.compile(start + "(.*?)" + end, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			apartmentsFound.add(matcher.group(1));
		}
		return apartmentsFound;
	}

}
