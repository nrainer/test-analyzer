package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/**
 * Interface for return value generators.<br/>
 * Implementing classes are instantiated using reflection.
 */
public interface IReturnValueGenerator {

	/** Get the name of the return value generator. */
	String getName();

	/** Put the byte code instructions to generate the return value. */
	void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier, Type returnType);

	/** Check if the return type is supported. */
	boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType);
}
