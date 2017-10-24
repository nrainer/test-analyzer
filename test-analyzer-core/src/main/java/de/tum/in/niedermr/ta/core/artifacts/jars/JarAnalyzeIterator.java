package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactAnalysisIterator;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;

/** Iterator for (read-only) analyzes. */
class JarAnalyzeIterator extends AbstractJarIterator<ICodeAnalyzeOperation> implements IArtifactAnalysisIterator {
	protected JarAnalyzeIterator(String inputJarPath, IArtifactExceptionHandler exceptionHandler) {
		super(inputJarPath, exceptionHandler);
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
}
