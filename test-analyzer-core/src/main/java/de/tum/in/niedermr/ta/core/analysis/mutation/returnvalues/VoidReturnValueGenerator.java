package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.Identification;

/** Return value generator for void methods. */
public class VoidReturnValueGenerator extends AbstractReturnValueGenerator {

	/** {@inheritDoc} */
	@Override
	public void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier,
			Type returnType) {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType) {
		return Identification.isVoid(returnType);
	}
}
