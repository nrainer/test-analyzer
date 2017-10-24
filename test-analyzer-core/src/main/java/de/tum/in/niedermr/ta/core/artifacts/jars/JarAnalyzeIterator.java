package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.iterator.IteratorException;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;

/** Iterator for (read-only) analyzes. */
public class JarAnalyzeIterator extends AbstractJarIterator<ICodeAnalyzeOperation> {
	protected JarAnalyzeIterator(String inputJarPath) {
		super(inputJarPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void beforeAll() throws IteratorException, IOException {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void handleEntry(ICodeAnalyzeOperation operation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException {
		operation.analyze(cr, originalClassPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void handleResource(ICodeAnalyzeOperation jarOperation, JarEntry resourceEntry, InputStream inStream)
			throws IteratorException, CodeOperationException, IOException {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void afterAll() throws IteratorException, IOException {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleEntry(Throwable throwable, String className) throws IteratorException {
		throw new IteratorException("Exception in handle entry", throwable);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleResource(Throwable throwable, String resourcePath) throws IteratorException {
		throw new IteratorException("Exception in handle resource", throwable);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInJarProcessing(Throwable throwable, ICodeAnalyzeOperation jarOperation)
			throws IteratorException {
		throw new IteratorException("Exception in jar processing", throwable);
	}
}
