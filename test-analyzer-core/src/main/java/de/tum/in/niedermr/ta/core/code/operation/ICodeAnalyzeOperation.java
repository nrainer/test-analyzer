package de.tum.in.niedermr.ta.core.code.operation;

import org.objectweb.asm.ClassReader;

/** Operation to analyze code. */
public interface ICodeAnalyzeOperation extends ICodeOperation {

	public void analyze(ClassReader cr, String originalClassPath) throws CodeOperationException;

	/** Clear the result. */
	public void clearResult();
}
