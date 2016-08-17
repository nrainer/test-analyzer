package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.instrumentation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogger;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractTryFinallyMethodVisitor;

public class AnalysisInstrumentationMethodVisitor extends AbstractTryFinallyMethodVisitor {
	/** Class path of {@link StackLogger}. */
	private static final String STACK_LOGGER_CLASS_PATH = JavaUtility.toClassPathWithoutEnding(StackLogger.class);

	private final MethodIdentifier m_identifier;

	public AnalysisInstrumentationMethodVisitor(MethodVisitor mv, String className, String methodName, String desc) {
		super(Opcodes.ASM5, mv);

		this.m_identifier = MethodIdentifier.create(className, methodName, desc);
	}

	@Override
	protected void execVisitBeforeFirstTryCatchBlock() {
		visitLdcInsn(m_identifier.get());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, STACK_LOGGER_CLASS_PATH, "pushInvocation", "(Ljava/lang/String;)V",
				false);
	}

	@Override
	protected void execVisitFinallyBlock() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, STACK_LOGGER_CLASS_PATH, "popInvocation", "()V", false);
	}
}
