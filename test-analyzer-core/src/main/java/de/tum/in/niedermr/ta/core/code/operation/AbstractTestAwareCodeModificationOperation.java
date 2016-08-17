package de.tum.in.niedermr.ta.core.code.operation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

/** Base class for a code modification operation that is aware of test classes. */
public abstract class AbstractTestAwareCodeModificationOperation implements ICodeModificationOperation {

	private final ITestClassDetector m_testClassDetector;

	/** Constructor. */
	public AbstractTestAwareCodeModificationOperation(ITestClassDetector testClassDetector) {
		m_testClassDetector = testClassDetector;
	}

	/** {@link #m_testClassDetector} */
	protected ITestClassDetector getTestClassDetector() {
		return m_testClassDetector;
	}

	/** {@inheritDoc} */
	@Override
	public void modify(ClassReader cr, ClassWriter cw) {
		ClassType classType = analyzeClassType(cr);

		if (classType.isTestClass()) {
			modifyTestClass(cr, cw, classType);
		} else {
			modifyNonTestClass(cr, cw);
		}
	}

	protected abstract void modifyNonTestClass(ClassReader cr, ClassWriter cw);

	protected abstract void modifyTestClass(ClassReader cr, ClassWriter cw, ClassType classType);

	private ClassType analyzeClassType(ClassReader cr) {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		return m_testClassDetector.analyzeIsTestClass(cn);
	}
}
