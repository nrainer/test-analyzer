package de.tum.in.niedermr.ta.core.code.operation;

import org.objectweb.asm.ClassReader;

public interface ICodeAnalyzeOperation extends ICodeOperation {
	public void analyze(ClassReader cr, String originalClassPath) throws Exception;
}
