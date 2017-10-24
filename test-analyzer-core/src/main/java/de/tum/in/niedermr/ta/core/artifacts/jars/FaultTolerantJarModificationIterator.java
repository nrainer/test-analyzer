package de.tum.in.niedermr.ta.core.artifacts.jars;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Fault tolerant wrapper for {@link JarModificationIterator}. */
public class FaultTolerantJarModificationIterator extends JarModificationIterator {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(FaultTolerantJarModificationIterator.class);

	/** Constructor. */
	protected FaultTolerantJarModificationIterator(String inputJarPath, String outputJarPath) {
		super(inputJarPath, outputJarPath);
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
	protected void onExceptionInJarProcessing(Throwable throwable, ICodeModificationOperation jarOperation) {
		LOGGER.error("Skipping whole jar file in fault tolerant mode because of a failure: " + getInputJarPath(),
				throwable);
	}
}
