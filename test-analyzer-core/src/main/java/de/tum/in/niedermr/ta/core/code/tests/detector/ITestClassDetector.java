package de.tum.in.niedermr.ta.core.code.tests.detector;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public interface ITestClassDetector {
	ClassType analyzeIsTestClass(ClassNode cn);

	boolean analyzeIsTestcase(MethodNode methodNode, ClassType testClassType);

	ClassLoader getClassLoader();
}
