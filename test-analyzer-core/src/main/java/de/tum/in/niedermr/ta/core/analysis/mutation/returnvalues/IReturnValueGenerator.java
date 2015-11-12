package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/**
 * The implementing class needs a constructor without parameters because this class will be instantiated using reflection.
 */
public interface IReturnValueGenerator {

	String getName();

	void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier, Type returnType);

	boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType);
}
