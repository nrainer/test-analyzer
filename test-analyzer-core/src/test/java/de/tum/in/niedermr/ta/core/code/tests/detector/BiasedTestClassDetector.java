package de.tum.in.niedermr.ta.core.code.tests.detector;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/** Returns the constant result specified in the constructor. */
public class BiasedTestClassDetector implements ITestClassDetector {

	/** Class type to return in {@link #analyzeIsTestClass(ClassNode)}. */
	private final ClassType m_classType;
	private final ClassLoader m_classLoader;

	/** Constructor. */
	public BiasedTestClassDetector(ClassType classType, ClassLoader classLoader) {
		m_classType = classType;
		m_classLoader = classLoader;
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

	/** {@inheritDoc} */
	@Override
	public ClassLoader getClassLoader() {
		return m_classLoader;
	}
}
