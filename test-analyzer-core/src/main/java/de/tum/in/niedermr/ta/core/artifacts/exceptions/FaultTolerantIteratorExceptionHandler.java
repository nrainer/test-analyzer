package de.tum.in.niedermr.ta.core.artifacts.exceptions;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Fault tolerant exception handler. */
public class FaultTolerantIteratorExceptionHandler implements IArtifactExceptionHandler {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(FaultTolerantIteratorExceptionHandler.class);

	/** {@inheritDoc} */
	@Override
	public void onExceptionInHandleClass(Throwable throwable, IArtifactIterator<?> iterator,
			ClassReader classInputReader, String originalClassPath) {
		LOGGER.warn("Skipping " + JavaUtility.toClassName(classInputReader.getClassName()) + " in fault tolerant mode. "
				+ throwable.getClass().getName() + " occurred with message '" + throwable.getMessage() + "'.");
	}

	/** {@inheritDoc} */
	@Override
	public void onExceptionInHandleResource(Throwable throwable, IArtifactIterator<?> iterator, InputStream inputStream,
			String resourcePath) {
		LOGGER.warn("Skipping resource " + resourcePath + " in fault tolerant mode. " + throwable.getClass().getName()
				+ " occurred with message '" + throwable.getMessage() + "'.");
	}

	/** {@inheritDoc} */
	@Override
	public void onExceptionInArtifactIteration(Throwable throwable, IArtifactIterator<?> iterator,
			ICodeOperation operation, String artifactContainer) {
		LOGGER.error("Skipping artifact processing in fault tolerant mode because of a failure: " + artifactContainer,
				throwable);
		operation.reset();
	}
}
