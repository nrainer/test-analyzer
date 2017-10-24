package de.tum.in.niedermr.ta.core.artifacts.exceptions;

import java.io.InputStream;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Exception handler. */
public interface IArtifactExceptionHandler {

	/**
	 * On exception in handleResource.
	 */
	void onExceptionInHandleResource(Throwable throwable, IArtifactIterator<?> iterator, InputStream inputStream,
			String resourcePath) throws IteratorException;

	/**
	 * On exception in handleClass.
	 */
	void onExceptionInHandleClass(Throwable throwable, IArtifactIterator<?> iterator, ClassReader classInputReader,
			String originalClassPath) throws IteratorException;

	/**
	 * An exception occurred during the preparation or tear down (outside of
	 * entry or resource handling). <br/>
	 * {@link #beforeAll()} and {@link #afterAll()} of the artifact iterator may
	 * not have been invoked.
	 * 
	 * @param throwable
	 *            thrown exception
	 * @param operation
	 *            operation to be executed
	 */
	void onExceptionInArtifactIteration(Throwable throwable, IArtifactIterator<?> iterator, ICodeOperation operation,
			String artifactContainer) throws IteratorException;

}
