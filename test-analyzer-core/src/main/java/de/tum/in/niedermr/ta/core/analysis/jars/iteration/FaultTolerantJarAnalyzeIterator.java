package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Fault tolerant wrapper for {@link JarAnalyzeIterator}. */
public class FaultTolerantJarAnalyzeIterator extends JarAnalyzeIterator {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(FaultTolerantJarAnalyzeIterator.class);

	/** Constructor. */
	protected FaultTolerantJarAnalyzeIterator(String inputJarPath) {
		super(inputJarPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleEntry(Throwable t, String className) {
		LOGGER.warn("Skipping " + JavaUtility.toClassName(className) + " in fault tolerant mode. "
				+ t.getClass().getName() + " occurred with message '" + t.getMessage() + "'.");
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleResource(Throwable t, String resourcePath) {
		LOGGER.warn("Skipping resource " + resourcePath + " in fault tolerant mode. " + t.getClass().getName()
				+ " occurred with message '" + t.getMessage() + "'.");
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInJarProcessing(Throwable throwable, ICodeAnalyzeOperation jarOperation) {
		LOGGER.error("Skipping whole jar file in fault tolerant mode because of a failure: " + getInputJarPath(),
				throwable);
		jarOperation.clearResult();
	}
}
