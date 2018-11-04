package de.tum.in.niedermr.ta.sdist.maven.status;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

public class SimpleInstrumentationStatusManager implements IInstrumentationStatusManager {

	protected final Log logger;

	public SimpleInstrumentationStatusManager(Log logger) {
		this.logger = logger;
	}

	@Override
	public boolean checkIsInstrumented(String codeDirectory) {
		logger.info("Checking instrumentation status using: " + getClass().getSimpleName());
		return createInstrumentedMarkerFile(codeDirectory).exists();
	}

	@Override
	public void markAsInstrumented(String codeDirectory) throws IOException {
		if (checkIsInstrumented(codeDirectory)) {
			return;
		}

		File markerFile = createInstrumentedMarkerFile(codeDirectory);
		logger.info("Creating marker file: " + markerFile.getAbsolutePath());
		markerFile.createNewFile();
	}

	protected File createInstrumentedMarkerFile(String codeDirectory) {
		return new File(codeDirectory, "sdist-instrumented.info");
	}
}
