package de.tum.in.niedermr.ta.core.code.operation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

/** Base class for a code operation that is aware of test classes. */
abstract class AbstractTestAwareCodeOperation implements ICodeOperation {

	private final ITestClassDetector m_testClassDetector;

	/** Constructor. */
	public AbstractTestAwareCodeOperation(ITestClassDetector testClassDetector) {
		m_testClassDetector = testClassDetector;
	}

	/** {@link #m_testClassDetector} */
	protected ITestClassDetector getTestClassDetector() {
		return m_testClassDetector;
	}

	/** Analyze the type of a given class. */
	protected ClassType analyzeClassType(ClassReader cr) {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		return analyzeClassType(cn);
	}

	/** Analyze the type of a given class. */
	protected ClassType analyzeClassType(ClassNode cn) {
		return m_testClassDetector.analyzeIsTestClass(cn);
	}
}
