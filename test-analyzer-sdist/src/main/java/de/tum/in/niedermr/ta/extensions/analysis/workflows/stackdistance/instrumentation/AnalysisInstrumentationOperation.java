package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

public class AnalysisInstrumentationOperation extends AbstractTestAwareCodeModificationOperation {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AnalysisInstrumentationOperation.class);

	/** Class that records the data gathered from the instrumentation. */
	private final Class<?> m_instrumentationDataRetrieverClass;

	private boolean m_failIfAlreadyInstrumented = false;

	/** Constructor. */
	public AnalysisInstrumentationOperation(ITestClassDetector testClassDetector,
			Class<?> instrumentationDataRetrieverClass) {
		super(testClassDetector);
		m_instrumentationDataRetrieverClass = instrumentationDataRetrieverClass;
	}

	public void setFailIfAlreadyInstrumented(boolean failIfAlreadyInstrumented) {
		this.m_failIfAlreadyInstrumented = failIfAlreadyInstrumented;
	}

	public boolean isFailIfAlreadyInstrumented() {
		return m_failIfAlreadyInstrumented;
	}

	/** {@inheritDoc} */
	@Override
	protected void modifySourceClass(ClassReader cr, ClassWriter cw) {
		if (checkIsAlreadyInstrumented(cr)) {
			if (m_failIfAlreadyInstrumented) {
				throw new IllegalStateException("Class is already instrumented: " + cr.getClassName());
			}

			LOGGER.warn("Skipping already instrumented class: " + cr.getClassName());
			// must be done to avoid empty class files
			super.modifySourceClass(cr, cw);
			return;
		}

		ClassVisitor cv = new AnalysisInstrumentationClassVisitor(cw, cr, m_instrumentationDataRetrieverClass);
		cr.accept(cv, ClassReader.EXPAND_FRAMES);
	}

	protected boolean checkIsAlreadyInstrumented(ClassReader cr) {
		AlreadyInstrumentationClassVisitor alreadyInstrumentedVisitor = new AlreadyInstrumentationClassVisitor(cr,
				m_instrumentationDataRetrieverClass);
		cr.accept(alreadyInstrumentedVisitor, 0);
		return alreadyInstrumentedVisitor.isAlreadyInstrumented();
	}
}
