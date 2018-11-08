package de.tum.in.niedermr.ta.sdist.maven.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;

/** Advanced instrumentation manager that checks if any class file was changed since the last instrumentation. */
public class AdvancedInstrumentationStatusManager extends SimpleInstrumentationStatusManager {

	/** Constructor. */
	public AdvancedInstrumentationStatusManager(Log logger) {
		super(logger);
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkIsInstrumented(String codeDirectory) throws IOException {
		if (!super.checkIsInstrumented(codeDirectory)) {
			return false;
		}

		List<String> instrumentationInfo = TextFileUtility
				.readFromFile(getInstrumentedMarkerFile(codeDirectory).getAbsolutePath());
		long lastInstrumentedTimestamp = getLastInstrumentedTimestampFromInfoFile(instrumentationInfo);

		if (lastInstrumentedTimestamp <= 0) {
			logger.warn("Last instrumented timestamp is invalid." + " Assuming not instrumented.");
			return false;
		}

		long lastChangeTimestamp = computeLastChangeTimestamp(codeDirectory);

		if (lastChangeTimestamp < lastInstrumentedTimestamp) {
			// do not use <= because timestamps are truncated to seconds
			logger.info("Already instrumented at " + lastInstrumentedTimestamp + " and unchanged since "
					+ lastChangeTimestamp + ".");
			return true;
		}

		logger.warn("Instrumentation file is present, but files changed (" + lastChangeTimestamp
				+ ") after the last instrumentation (" + lastInstrumentedTimestamp + ")!"
				+ " It is possible that the files were recompiled."
				+ " Files will be instrumented if not instrumented yet.");
		return false;
	}

	private long computeLastChangeTimestamp(String codeDirectory) throws IOException {
		return Files.walk(Paths.get(codeDirectory)).filter(path -> isRelevantCompiledClass(path))
				.mapToLong(path -> getLastModifiedTimestamp(path)).max().orElse(-1);
	}

	protected boolean isRelevantCompiledClass(Path path) {
		if (!path.toString().endsWith(".class")) {
			return false;
		}

		if (path.toString().endsWith("package-info.class")) {
			return false;
		}

		return true;
	}

	private long getLastModifiedTimestamp(Path path) {
		try {
			FileTime lastModifiedTime = Files.getLastModifiedTime(path);
			return lastModifiedTime.toMillis();
		} catch (IOException e) {
			logger.warn("Failed to retrieve timestamp of " + path, e);
			return -1;
		}
	}

	private long getLastInstrumentedTimestampFromInfoFile(List<String> instrumentationInfo) {
		if (instrumentationInfo.isEmpty()) {
			return -1;
		}

		String firstLine = instrumentationInfo.get(0).trim();

		if (firstLine.isEmpty()) {
			return -1;
		}

		return Long.parseLong(firstLine);
	}

	/** {@inheritDoc} */
	@Override
	public void markAsInstrumented(String codeDirectory) throws IOException {
		super.markAsInstrumented(codeDirectory);

		File instrumentationInfoFile = getInstrumentedMarkerFile(codeDirectory);
		long currentTimestamp = new Date().getTime();

		// reduce resolution to seconds (because the Linux filesystem operates on seconds)
		currentTimestamp = (currentTimestamp / 1000L) * 1000L;

		logger.info("Updating marker file with timestamp " + currentTimestamp + ": "
				+ instrumentationInfoFile.getAbsolutePath());
		TextFileUtility.writeToFile(instrumentationInfoFile.getAbsolutePath(),
				Arrays.asList(String.valueOf(currentTimestamp)));
	}
}
