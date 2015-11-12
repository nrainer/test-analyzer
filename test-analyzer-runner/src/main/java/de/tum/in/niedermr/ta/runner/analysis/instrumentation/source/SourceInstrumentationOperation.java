package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.source.bytecode.InstrumentationClassVisitor;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.TestInstrumentationOperation;

/**
 * This operation <b>instruments source jar files.</b> It instruments the <b>source classes and contained test classes</b> as well. <br/>
 * <br/>
 * <b>Test classes in source jar files must be handled like test classes in test jars.</b> The reason is that the source and test jars might be the same and the
 * source jar is in an earlier position in the classpath, thus its classes would hide the instrumented test classes in the test jar.
 *
 */
public class SourceInstrumentationOperation implements ICodeModificationOperation {
	private final ITestClassDetector m_testClassDetector;
	private final TestInstrumentationOperation m_testInstrumentationOperation;

	public SourceInstrumentationOperation(ITestClassDetector testClassDetector, TestInstrumentationOperation testInstrumentationOperation) {
		this.m_testClassDetector = testClassDetector;
		this.m_testInstrumentationOperation = testInstrumentationOperation;
	}

	@Override
	public void modify(ClassReader cr, ClassWriter cw) throws Exception {
		if (isTestClass(cr)) {
			m_testInstrumentationOperation.modify(cr, cw);
		} else {
			ClassVisitor cv = new InstrumentationClassVisitor(cw, cr.getClassName());
			cr.accept(cv, 0);
		}
	}

	private boolean isTestClass(ClassReader cr) {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		return m_testClassDetector.analyzeIsTestClass(cn).isTestClass();
	}
}
