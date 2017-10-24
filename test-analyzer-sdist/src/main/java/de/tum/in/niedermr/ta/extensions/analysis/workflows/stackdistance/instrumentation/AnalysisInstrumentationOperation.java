package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

public class AnalysisInstrumentationOperation extends AbstractTestAwareCodeModificationOperation {

	/** Class that records the data gathered from the instrumentation. */
	private final Class<?> m_instrumentationDataRetrieverClass;

	/** Constructor. */
	public AnalysisInstrumentationOperation(ITestClassDetector testClassDetector,
			Class<?> instrumentationDataRetrieverClass) {
		super(testClassDetector);
		m_instrumentationDataRetrieverClass = instrumentationDataRetrieverClass;
	}

	/** {@inheritDoc} */
	@Override
	protected void modifySourceClass(ClassReader cr, ClassWriter cw) {
		ClassVisitor cv = new AnalysisInstrumentationClassVisitor(cw, cr.getClassName(),
				m_instrumentationDataRetrieverClass);
		cr.accept(cv, ClassReader.EXPAND_FRAMES);
	}
}
