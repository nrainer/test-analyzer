package de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.detector;

import java.util.List;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.AbstractTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;

public class TestNgTestClassDetector extends AbstractTestClassDetector {
	private static final String TEST_ANNOTATION = "Lorg/testng/annotations/Test;";
	private static final String TEST_ANNOTATION_VALUE_ENABLED = "enabled";

	public TestNgTestClassDetector(boolean acceptAbstractTestClasses, String[] testClassIncludes,
			String[] testClassExcludes) {
		super(acceptAbstractTestClasses, testClassIncludes, testClassExcludes);
	}

	@Override
	protected ClassType isTestClassInternal(ClassNode cn) {
		if (isTestNgTestClass(cn)) {
			return ClassType.TEST_CLASS;
		} else {
			return ClassType.NO_TEST_CLASS;
		}
	}

	@Override
	public boolean analyzeIsTestcase(MethodNode methodNode, ClassType testClassType) {
		return testClassType.isTestClass() && isTestNgTestMethod(methodNode);
	}

	@SuppressWarnings("unchecked")
	private boolean isTestNgTestClass(ClassNode cn) {
		for (MethodNode method : (List<MethodNode>) cn.methods) {
			if (isTestNgTestMethod(method)) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean isTestNgTestMethod(MethodNode method) {
		if (method.visibleAnnotations == null) {
			return false;
		}

		for (AnnotationNode annotation : (List<AnnotationNode>) method.visibleAnnotations) {
			if (annotation.desc.contains(TEST_ANNOTATION)) {
				if (!isDisabledTest(annotation.values)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @param annotationValues
	 *            Alternating sequence of name and value.
	 */
	private boolean isDisabledTest(List<Object> annotationValues) {
		if (annotationValues == null) {
			return false;
		}

		boolean previousWasEnabledName = false;

		for (Object x : annotationValues) {
			if (previousWasEnabledName) {
				return x.equals(Boolean.TRUE);
			} else if (x.equals(TEST_ANNOTATION_VALUE_ENABLED)) {
				previousWasEnabledName = true;
			}
		}

		return false;
	}
}
