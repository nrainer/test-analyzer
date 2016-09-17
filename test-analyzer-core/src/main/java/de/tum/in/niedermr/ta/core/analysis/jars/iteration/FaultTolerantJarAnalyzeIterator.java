package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class FaultTolerantJarAnalyzeIterator extends JarAnalyzeIterator {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(FaultTolerantJarAnalyzeIterator.class);

	protected FaultTolerantJarAnalyzeIterator(String inputJarPath) {
		super(inputJarPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleEntry(Throwable t, String className) throws Exception {
		LOGGER.warn("Skipping " + JavaUtility.toClassName(className) + " in fault tolerant mode. "
				+ t.getClass().getName() + " occurred with message '" + t.getMessage() + "'.");
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleResource(Throwable t, String resourcePath) throws Exception {
		LOGGER.warn("Skipping resource " + resourcePath + " in fault tolerant mode. " + t.getClass().getName()
				+ " occurred with message '" + t.getMessage() + "'.");
	}
}
