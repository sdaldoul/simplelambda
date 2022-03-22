package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class OfferState {

	public static final String STATEFILENAME = "/tmp/location.txt";

	private LambdaLogger logger;
	private File fileName;
	private Path fileNamePath;

	public OfferState() {
		this.fileName = new File(STATEFILENAME);
		try {
			this.fileName.createNewFile(); // if file already exists will do nothing
			this.fileNamePath = this.fileName.toPath();
		} catch (IOException e) {
			logger.log("IOException OfferState() , getMessage: " +e.getMessage());
		}
	}

	public void setLogger(LambdaLogger logger) {
		this.logger = logger;
	}

	public OfferState(Context context) {
		this.logger = context.getLogger();
		this.fileName = new File(STATEFILENAME);
		try {
			this.fileName.createNewFile(); // if file already exists will do nothing
			this.fileNamePath = this.fileName.toPath();
		} catch (IOException e) {
			logger.log("IOException OfferState() , getMessage: " +e.getMessage());
		}
	}

	public String getActualState() throws IOException {
		String actual = Files.readString(this.fileNamePath);
		logger.log("getActualState : " + actual + "\n");
    return actual;
	}

	public void saveState(String state) throws IOException {
		logger.log("saveState : " + state + "\n");
		Files.writeString(this.fileNamePath, state);
	}

	public void saveState(Set<String> emailSet) throws IOException {
		logger.log("saveState : " + emailSet + "\n");
		Files.writeString(this.fileNamePath, emailSet.toString());
	}

	public Set<String> getEmailState(Set<String> apartmentsFound, String actual)  {
		Set<String> emailSet = apartmentsFound.stream().filter(s -> !actual.contains(s)).collect(Collectors.toSet());
		logger.log("getEmailState : " + emailSet + "\n");
		return emailSet;
	}





}
