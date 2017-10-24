package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import de.tum.in.niedermr.ta.core.code.visitor.AbstractCommonClassVisitor;

public class AnalysisInstrumentationClassVisitor extends AbstractCommonClassVisitor {

	/** Class that records the data gathered from the instrumentation. */
	private final Class<?> m_instrumentationDataRetrieverClass;

	/** Constructor. */
	public AnalysisInstrumentationClassVisitor(ClassVisitor cv, String className,
			Class<?> instrumentationDataRetrieverClass) {
		super(cv, className);
		m_instrumentationDataRetrieverClass = instrumentationDataRetrieverClass;
	}

	/** {@inheritDoc} */
	@Override
	protected MethodVisitor visitNonConstructorMethod(MethodVisitor mv, int access, String methodName, String desc) {
		return new AnalysisInstrumentationMethodVisitor(mv, getClassName(), methodName, desc,
				m_instrumentationDataRetrieverClass);
	}
}
