package de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger;
import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger.LoggingMode;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.code.visitor.AbstractTryFinallyMethodVisitor;

public class TestModeMethodVisitor extends AbstractTryFinallyMethodVisitor implements Opcodes {
	private static final String CP_INVOCATION_LOGGER = JavaUtility.toClassPathWithoutEnding(InvocationLogger.class);
	private static final String CP_LOGGING_MODE = JavaUtility
			.toClassPathWithoutEnding(InvocationLogger.LoggingMode.class);
	private static final String FIELD_NAME_FRAMING = "FRAMING";
	private static final String FIELD_NAME_TESTING = "TESTING";
	private static final String FIELD_DESC_LOGGING_MODE = "L" + CP_LOGGING_MODE + ";";
	private static final String METHOD_NAME_SET_MODE = "setMode";
	private static final String METHOD_DESC_SET_MODE = "(L" + CP_LOGGING_MODE + ";)V";

	public TestModeMethodVisitor(MethodVisitor mv, ClassNode cn, String name, String desc) {
		super(Opcodes.ASM5, mv, cn, name, desc);
	}

	@Override
	protected void execVisitCodeBeforeFirstTryCatchBlock() {
		insertInstructionToSetMode(LoggingMode.TESTING);
	}

	@Override
	protected void execVisitCodeInFinallyBlock() {
		insertInstructionToSetMode(LoggingMode.FRAMING);
	}

	private void insertInstructionToSetMode(LoggingMode mode) {
		String modeValue;

		if (mode == LoggingMode.TESTING) {
			modeValue = FIELD_NAME_TESTING;
		} else {
			modeValue = FIELD_NAME_FRAMING;
		}

		visitFieldInsn(Opcodes.GETSTATIC, CP_LOGGING_MODE, modeValue, FIELD_DESC_LOGGING_MODE);
		visitMethodInsn(INVOKESTATIC, CP_INVOCATION_LOGGER, METHOD_NAME_SET_MODE, METHOD_DESC_SET_MODE, false);
	}
}
