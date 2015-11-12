package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source.bytecode;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import de.tum.in.niedermr.ta.core.code.visitor.AbstractCommonClassVisitor;

public class InstrumentationClassVisitor extends AbstractCommonClassVisitor {
	public InstrumentationClassVisitor(ClassVisitor cv, String className) {
		super(cv, className);
	}

	@Override
	protected MethodVisitor visitNonConstructorMethod(MethodVisitor mv, int access, String methodName, String desc) {
		return new InstrumentationMethodVisitor(mv, getClassName(), methodName, desc);
	}
}
