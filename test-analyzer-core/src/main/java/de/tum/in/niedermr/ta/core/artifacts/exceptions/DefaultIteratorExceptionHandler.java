package de.tum.in.niedermr.ta.core.artifacts.exceptions;

import java.io.InputStream;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Default exception handler. */
public class DefaultIteratorExceptionHandler implements IArtifactExceptionHandler {

	/** {@inheritDoc} */
	@Override
	public void onExceptionInHandleResource(Throwable throwable, IArtifactIterator<?> iterator, InputStream inputStream,
			String resourcePath) throws IteratorException {
		throw new IteratorException("Exception in jar processing", throwable);
	}

	/** {@inheritDoc} */
	@Override
	public void onExceptionInHandleClass(Throwable throwable, IArtifactIterator<?> iterator,
			ClassReader classInputReader, String originalClassPath) throws IteratorException {
		throw new IteratorException("Exception in jar processing", throwable);
	}

	/** {@inheritDoc} */
	@Override
	public void onExceptionInArtifactIteration(Throwable throwable, IArtifactIterator<?> iterator,
			ICodeOperation operation, String artifactContainer) throws IteratorException {
		throw new IteratorException("Exception in jar processing", throwable);
	}
}
