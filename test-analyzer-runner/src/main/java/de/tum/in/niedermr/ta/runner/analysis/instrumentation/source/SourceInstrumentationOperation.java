package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.source.bytecode.InstrumentationClassVisitor;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.TestInstrumentationOperation;

/**
 * This operation <b>instruments source jar files.</b> It instruments the <b>source classes and contained test
 * classes</b> as well. <br/>
 * <br/>
 * <b>Test classes in source jar files must be handled like test classes in test jars.</b> The reason is that the source
 * and test jars might be the same and the source jar is in an earlier position in the classpath, thus its classes would
 * hide the instrumented test classes in the test jar.
 *
 */
public class SourceInstrumentationOperation extends AbstractTestAwareCodeModificationOperation {
	private final TestInstrumentationOperation m_testInstrumentationOperation;

	/** Constructor. */
	public SourceInstrumentationOperation(ITestClassDetector testClassDetector,
			TestInstrumentationOperation testInstrumentationOperation) {
		super(testClassDetector);
		this.m_testInstrumentationOperation = testInstrumentationOperation;
	}

	/** {@inheritDoc} */
	@Override
	protected void modifySourceClass(ClassReader cr, ClassWriter cw) {
		ClassVisitor cv = new InstrumentationClassVisitor(cw, cr.getClassName());
		cr.accept(cv, 0);
	}

	/** {@inheritDoc} */
	@Override
	protected void modifyTestClass(ClassReader cr, ClassWriter cw, ClassType classType) {
		m_testInstrumentationOperation.modify(cr, cw);
	}
}
