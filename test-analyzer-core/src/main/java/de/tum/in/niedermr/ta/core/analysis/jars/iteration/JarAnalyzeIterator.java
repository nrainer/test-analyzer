package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;

/** Iterator for (read-only) analyzes. */
public class JarAnalyzeIterator extends AbstractJarIterator<ICodeAnalyzeOperation> {
	protected JarAnalyzeIterator(String inputJarPath) {
		super(inputJarPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void beforeAll() {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void handleEntry(ICodeAnalyzeOperation operation, ClassReader cr, String originalClassPath)
			throws Exception {
		operation.analyze(cr, originalClassPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void handleResource(ICodeAnalyzeOperation jarOperation, JarEntry resourceEntry, InputStream inStream) {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void afterAll() {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleEntry(Throwable throwable, String className) throws Exception {
		throw new Exception(throwable);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleResource(Throwable throwable, String resourcePath) throws Exception {
		throw new Exception(throwable);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInJarProcessing(Throwable throwable, ICodeAnalyzeOperation jarOperation)
			throws Exception {
		throw new Exception(throwable);

	}
}
