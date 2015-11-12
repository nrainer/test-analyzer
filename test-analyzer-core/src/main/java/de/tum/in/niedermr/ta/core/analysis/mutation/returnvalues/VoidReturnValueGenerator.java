package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.Identification;

public class VoidReturnValueGenerator extends AbstractReturnValueGenerator {
	@Override
	public void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier, Type returnType) {
		// NOP
	}

	@Override
	public boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType) {
		return Identification.isVoid(returnType);
	}
}
