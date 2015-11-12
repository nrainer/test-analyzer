package de.tum.in.niedermr.ta.runner.analysis.jars.iteration;

import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class FaultTolerantJarAnalyzeIterator extends JarAnalyzeIterator {
	private final Logger log;

	public FaultTolerantJarAnalyzeIterator(String inputJarPath, Logger log) {
		super(inputJarPath);
		this.log = log;
	}

	@Override
	protected void onExceptionInHandleEntry(Exception t, String className) throws Exception {
		log.warn("Skipping " + JavaUtility.toClassName(className) + " in fault tolerant mode. " + t.getClass().getName() + " occurred with message '"
				+ t.getMessage() + "'.");
	}

	@Override
	protected void onExceptionInHandleResource(Exception t, String resourcePath) throws Exception {
		log.warn("Skipping resource " + resourcePath + " in fault tolerant mode. " + t.getClass().getName() + " occurred with message '" + t.getMessage()
				+ "'.");
	}
}
