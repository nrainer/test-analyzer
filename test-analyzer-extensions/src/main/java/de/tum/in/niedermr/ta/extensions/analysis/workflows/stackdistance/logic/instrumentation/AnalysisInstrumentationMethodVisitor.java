package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.instrumentation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogger;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractTryFinallyMethodVisitor;

public class AnalysisInstrumentationMethodVisitor extends AbstractTryFinallyMethodVisitor {
	private final MethodIdentifier identifier;

	public AnalysisInstrumentationMethodVisitor(MethodVisitor mv, String className, String methodName, String desc) {
		super(Opcodes.ASM5, mv);

		this.identifier = MethodIdentifier.create(className, methodName, desc);
	}

	@Override
	protected void execVisitBeforeFirstTryCatchBlock() {
		visitLdcInsn(identifier.get());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, JavaUtility.toClassPathWithoutEnding(StackLogger.class), "pushInvocation", "(Ljava/lang/String;)V", false);
	}

	@Override
	protected void execVisitFinallyBlock() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, JavaUtility.toClassPathWithoutEnding(StackLogger.class), "popInvocation", "()V", false);
	}
}
