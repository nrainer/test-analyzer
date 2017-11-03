package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueGenerator;
import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/**
 * Supports the creation of instances of classes which provide a constructor without parameters.
 */
public class SimpleInstancesReturnValueGenerator extends AbstractReturnValueGenerator {

	/** {@inheritDoc} */
	@Override
	public boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType) {
		try {
			JavaUtility.createInstance(returnType.getClassName());
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier,
			Type returnType) {
		mv.visitTypeInsn(Opcodes.NEW, returnType.getInternalName());
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, returnType.getInternalName(), BytecodeConstants.NAME_CONSTRUCTOR,
				BytecodeConstants.DESCRIPTOR_NO_PARAM_AND_VOID, false);
	}
}