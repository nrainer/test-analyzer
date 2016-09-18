package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Return value generator which can handle methods with primitive and String
 * return types. Void is not supported. Note that wrappers of primitive types
 * are not supported!
 *
 */
public class SimpleReturnValueGeneratorWith0 extends AbstractSimpleReturnValueGenerator {

	/** Constructor. */
	public SimpleReturnValueGeneratorWith0() {
		super(true);
	}

	/** {@inheritDoc} */
	@Override
	public void handleBooleanReturn(MethodVisitor mv) {
		mv.visitInsn(Opcodes.ICONST_0);
	}

	/** {@inheritDoc} */
	@Override
	public void handleIntegerReturn(MethodVisitor mv) {
		mv.visitInsn(Opcodes.ICONST_0);
	}

	/** {@inheritDoc} */
	@Override
	public void handleLongReturn(MethodVisitor mv) {
		mv.visitInsn(Opcodes.LCONST_0);
	}

	/** {@inheritDoc} */
	@Override
	public void handleFloatReturn(MethodVisitor mv) {
		mv.visitInsn(Opcodes.FCONST_0);
	}

	/** {@inheritDoc} */
	@Override
	public void handleDoubleReturn(MethodVisitor mv) {
		mv.visitInsn(Opcodes.DCONST_0);
	}

	/** {@inheritDoc} */
	@Override
	public void handleCharReturn(MethodVisitor mv) {
		final int charValue = ' ';
		mv.visitIntInsn(Opcodes.BIPUSH, charValue);
	}

	/** {@inheritDoc} */
	@Override
	public void handleStringReturn(MethodVisitor mv) {
		mv.visitLdcInsn("");
	}
}
