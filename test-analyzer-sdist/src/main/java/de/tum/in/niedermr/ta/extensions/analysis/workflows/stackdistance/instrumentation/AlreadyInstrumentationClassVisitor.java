package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;

import de.tum.in.niedermr.ta.core.code.visitor.AbstractCommonClassVisitor;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation.AlreadyInstrumentedMethodVisitor.AlreadyInstrumentedHandler;

public class AlreadyInstrumentationClassVisitor extends AbstractCommonClassVisitor
		implements AlreadyInstrumentedHandler {

	/** Class that records the data gathered from the instrumentation. */
	private final Class<?> m_instrumentationDataRetrieverClass;
	private boolean m_isInstrumented;

	/** Constructor. */
	public AlreadyInstrumentationClassVisitor(ClassReader cr, Class<?> instrumentationDataRetrieverClass) {
		super(null, cr.getClassName());
		m_instrumentationDataRetrieverClass = instrumentationDataRetrieverClass;
	}

	/** {@inheritDoc} */
	@Override
	protected MethodVisitor visitNonConstructorMethod(MethodVisitor mv, int access, String methodName, String desc) {
		return new AlreadyInstrumentedMethodVisitor(mv, m_instrumentationDataRetrieverClass, this);
	}

	public boolean isAlreadyInstrumented() {
		return m_isInstrumented;
	}

	/** {@inheritDoc} */
	@Override
	public void execOnAlreadyInstrumented() {
		m_isInstrumented = true;
	}
}
