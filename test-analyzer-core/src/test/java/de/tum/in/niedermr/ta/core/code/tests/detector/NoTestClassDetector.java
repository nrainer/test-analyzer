package de.tum.in.niedermr.ta.core.code.tests.detector;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/** Detects no classes as test classes. */
public class NoTestClassDetector implements ITestClassDetector {

	/** {@inheritDoc} */
	@Override
	public ClassType analyzeIsTestClass(ClassNode cn) {
		return ClassType.NO_TEST_CLASS;
	}

	/** {@inheritDoc} */
	@Override
	public boolean analyzeIsTestcase(MethodNode methodNode, ClassType testClassType) {
		return false;
	}
}
