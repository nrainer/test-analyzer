package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class FaultTolerantJarAnalyzeIterator extends JarAnalyzeIterator {
	private static final Logger LOG = LogManager.getLogger(FaultTolerantJarAnalyzeIterator.class);

	protected FaultTolerantJarAnalyzeIterator(String inputJarPath) {
		super(inputJarPath);
	}

	@Override
	protected void onExceptionInHandleEntry(Throwable t, String className) throws Exception {
		LOG.warn("Skipping " + JavaUtility.toClassName(className) + " in fault tolerant mode. " + t.getClass().getName()
				+ " occurred with message '" + t.getMessage() + "'.");
	}

	@Override
	protected void onExceptionInHandleResource(Throwable t, String resourcePath) throws Exception {
		LOG.warn("Skipping resource " + resourcePath + " in fault tolerant mode. " + t.getClass().getName()
				+ " occurred with message '" + t.getMessage() + "'.");
	}
}
