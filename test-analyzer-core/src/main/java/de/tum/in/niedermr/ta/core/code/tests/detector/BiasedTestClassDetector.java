package de.tum.in.niedermr.ta.core.code.tests.detector;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/** Returns the constant result specified in the constructor. */
public class BiasedTestClassDetector implements ITestClassDetector {

	/** Class type to return in {@link #analyzeIsTestClass(ClassNode)}. */
	private ClassType m_classType;

	/** Constructor. */
	public BiasedTestClassDetector(ClassType classType) {
		m_classType = classType;
	}

	/** {@inheritDoc} */
	@Override
	public ClassType analyzeIsTestClass(ClassNode cn) {
		return m_classType;
	}

	/** {@inheritDoc} */
	@Override
	public boolean analyzeIsTestcase(MethodNode methodNode, ClassType testClassType) {
		return m_classType.isTestClass();
	}
}
