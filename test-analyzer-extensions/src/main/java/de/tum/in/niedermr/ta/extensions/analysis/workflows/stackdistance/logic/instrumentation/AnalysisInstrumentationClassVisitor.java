package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.instrumentation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import de.tum.in.niedermr.ta.core.code.visitor.AbstractCommonClassVisitor;

public class AnalysisInstrumentationClassVisitor extends AbstractCommonClassVisitor {
	public AnalysisInstrumentationClassVisitor(ClassVisitor cv, String className) {
		super(cv, className);
	}

	@Override
	protected MethodVisitor visitNonConstructorMethod(MethodVisitor mv, int access, String methodName, String desc) {
		return new AnalysisInstrumentationMethodVisitor(mv, getClassName(), methodName, desc);
	}
}
