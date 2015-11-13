package de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger;
import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger.LoggingMode;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractTryFinallyMethodVisitor;

public class TestModeMethodVisitor extends AbstractTryFinallyMethodVisitor implements Opcodes {
	private static final String CP_INVOCATION_LOGGER = JavaUtility.toClassPathWithoutEnding(InvocationLogger.class);
	private static final String CP_LOGGING_MODE = JavaUtility
			.toClassPathWithoutEnding(InvocationLogger.LoggingMode.class);
	private static final String FIELD_NAME_FRAMING = "FRAMING";
	private static final String FIELD_NAME_TESTING = "TESTING";
	private static final String FIELD_DESC_LOGGING_MODE = "L" + CP_LOGGING_MODE + ";";
	private static final String METHOD_NAME_SET_MODE = "setMode";
	private static final String METHOD_DESC_SET_MODE = "(L" + CP_LOGGING_MODE + ";)V";

	public TestModeMethodVisitor(MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}

	@Override
	protected void execVisitBeforeFirstTryCatchBlock() {
		insertInstructionToSetMode(LoggingMode.TESTING);
	}

	@Override
	protected void execVisitFinallyBlock() {
		insertInstructionToSetMode(LoggingMode.FRAMING);
	}

	private void insertInstructionToSetMode(LoggingMode mode) {
		String modeValue = (mode == LoggingMode.TESTING ? FIELD_NAME_TESTING : FIELD_NAME_FRAMING);

		visitFieldInsn(Opcodes.GETSTATIC, CP_LOGGING_MODE, modeValue, FIELD_DESC_LOGGING_MODE);
		visitMethodInsn(INVOKESTATIC, CP_INVOCATION_LOGGER, METHOD_NAME_SET_MODE, METHOD_DESC_SET_MODE, false);
	}
}
