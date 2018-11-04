package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.visitor.AbstractCommonClassVisitor;

public class AnalysisInstrumentationClassVisitor extends AbstractCommonClassVisitor {

	/** Class that records the data gathered from the instrumentation. */
	private final Class<?> m_instrumentationDataRetrieverClass;
	private final ClassReader m_cr;
	private boolean m_failIfAlreadyInstrumented;

	/** Constructor. */
	public AnalysisInstrumentationClassVisitor(ClassVisitor cv, ClassReader cr,
			Class<?> instrumentationDataRetrieverClass, boolean failIfAlreadyInstrumented) {
		super(cv, cr.getClassName());
		m_instrumentationDataRetrieverClass = instrumentationDataRetrieverClass;
		m_cr = cr;
		m_failIfAlreadyInstrumented = failIfAlreadyInstrumented;
	}

	/** {@inheritDoc} */
	@Override
	protected MethodVisitor visitNonConstructorMethod(MethodVisitor mv, int access, String methodName, String desc) {
		ClassNode cn = new ClassNode();
		m_cr.accept(cn, 0);

		MethodVisitor instrumentationMethodVisitor = new AnalysisInstrumentationMethodVisitor(mv, cn, getClassName(),
				methodName, desc, m_instrumentationDataRetrieverClass);

		if (m_failIfAlreadyInstrumented) {
			// wrap the method visitor
			return new AlreadyInstrumentedMethodVisitor(instrumentationMethodVisitor, getClassName(),
					m_instrumentationDataRetrieverClass);
		}

		return instrumentationMethodVisitor;
	}
}
