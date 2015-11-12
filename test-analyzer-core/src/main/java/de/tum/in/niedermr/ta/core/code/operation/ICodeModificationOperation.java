package de.tum.in.niedermr.ta.core.code.operation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public interface ICodeModificationOperation extends ICodeOperation {
	void modify(ClassReader cr, ClassWriter cw) throws Exception;
}
