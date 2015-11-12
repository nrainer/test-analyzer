package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class InstrumentationMethodVisitor extends MethodVisitor {
	private final MethodIdentifier m_identifier;

	public InstrumentationMethodVisitor(MethodVisitor mv, String className, String methodName, String desc) {
		super(Opcodes.ASM5, mv);

		this.m_identifier = MethodIdentifier.create(className, methodName, desc);
	}

	@Override
	public void visitCode() {
		super.visitCode();

		visitLdcInsn(m_identifier.get());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, JavaUtility.toClassPathWithoutEnding(InvocationLogger.class), "log", "(Ljava/lang/String;)V", false);
	}
}
