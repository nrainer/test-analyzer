package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class FaultTolerantJarModificationIterator extends JarModificationIterator {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(FaultTolerantJarModificationIterator.class);

	protected FaultTolerantJarModificationIterator(String inputJarPath, String outputJarPath) {
		super(inputJarPath, outputJarPath);
	}

	@Override
	protected void onExceptionInHandleEntry(Throwable t, String className) throws Exception {
		LOGGER.warn("Skipping " + JavaUtility.toClassName(className) + " in fault tolerant mode. "
				+ t.getClass().getName() + " occurred with message '" + t.getMessage() + "'.");
	}

	@Override
	protected void onExceptionInHandleResource(Throwable t, String resourcePath) throws Exception {
		LOGGER.warn("Skipping resource " + resourcePath + " in fault tolerant mode. " + t.getClass().getName()
				+ " occurred with message '" + t.getMessage() + "'.");
	}
}
