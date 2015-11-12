package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;

public class JarAnalyzeIterator extends AbstractJarIterator<ICodeAnalyzeOperation> {
	public JarAnalyzeIterator(String inputJarPath) {
		super(inputJarPath);
	}

	@Override
	protected void beforeAll() {
		// NOP
	}

	@Override
	protected void handleEntry(ICodeAnalyzeOperation operation, ClassReader cr, String originalClassPath)
			throws Exception {
		operation.analyze(cr, originalClassPath);
	}

	@Override
	protected void handleResource(ICodeAnalyzeOperation jarOperation, JarEntry resourceEntry, InputStream inStream) {
		// NOP
	}

	@Override
	protected void afterAll() {
		// NOP
	}

	@Override
	protected void onExceptionInHandleEntry(Exception ex, String className) throws Exception {
		throw ex;
	}

	@Override
	protected void onExceptionInHandleResource(Exception ex, String resourcePath) throws Exception {
		throw ex;
	}
}
