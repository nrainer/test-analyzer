package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueGenerator;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Return value generator that returns null for objects and arrays. */
public class NullReturnValueGenerator extends AbstractReturnValueGenerator {

	/** {@inheritDoc} */
	@Override
	public void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier,
			Type returnType) {
		mv.visitInsn(Opcodes.ACONST_NULL);
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType) {
		return returnType.getSort() == Type.OBJECT || returnType.getSort() == Type.ARRAY;
	}
}
