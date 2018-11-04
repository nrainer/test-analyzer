package de.tum.in.niedermr.ta.sdist.maven.status;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

public class SimpleInstrumentationStatusManager implements IInstrumentationStatusManager {

	protected final Log logger;

	public SimpleInstrumentationStatusManager(Log logger) {
		this.logger = logger;
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkIsInstrumented(String codeDirectory) throws IOException {
		return getInstrumentedMarkerFile(codeDirectory).exists();
	}

	/** {@inheritDoc} */
	@Override
	public void markAsInstrumented(String codeDirectory) throws IOException {
		if (checkIsInstrumented(codeDirectory)) {
			return;
		}

		File markerFile = getInstrumentedMarkerFile(codeDirectory);
		logger.info("Creating marker file: " + markerFile.getAbsolutePath());
		markerFile.createNewFile();
	}

	protected File getInstrumentedMarkerFile(String codeDirectory) {
		String string = "sdist-instrumented.info";
		return new File(codeDirectory, string);
	}
}
