package de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.detector;

import java.util.List;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.AbstractTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;

/** Test class detector for the testNG framework. */
public class TestNgTestClassDetector extends AbstractTestClassDetector {
	private static final String TEST_ANNOTATION = "Lorg/testng/annotations/Test;";
	private static final String TEST_ANNOTATION_VALUE_ENABLED = "enabled";

	/** Constructor. */
	public TestNgTestClassDetector(boolean acceptAbstractTestClasses, String[] testClassIncludes,
			String[] testClassExcludes) {
		super(acceptAbstractTestClasses, testClassIncludes, testClassExcludes);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	protected ClassType isTestClassInternal(ClassNode cn) {
		int countTestMethods = 0;
		int countDisabledTests = 0;

		for (MethodNode method : (List<MethodNode>) cn.methods) {
			if (isTestNgTestMethod(method)) {
				countTestMethods++;

				if (isDisabledTestcase(method)) {
					countDisabledTests++;
				}
			}
		}

		if (countTestMethods == 0) {
			return ClassType.NO_TEST_CLASS;
		}

		if (countTestMethods == countDisabledTests) {
			return ClassType.IGNORED_TEST_CLASS;
		}

		return ClassType.TEST_CLASS;
	}

	/** {@inheritDoc} */
	@Override
	public boolean analyzeIsTestcase(MethodNode methodNode, ClassType testClassType) {
		return testClassType.isTestClass() && isTestNgTestMethod(methodNode) && !isDisabledTestcase(methodNode);
	}

	@SuppressWarnings("unchecked")
	private boolean isTestNgTestMethod(MethodNode method) {
		if (method.visibleAnnotations == null) {
			return false;
		}

		for (AnnotationNode annotation : (List<AnnotationNode>) method.visibleAnnotations) {
			if (annotation.desc.contains(TEST_ANNOTATION)) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean isDisabledTestcase(MethodNode method) {
		List<Object> testAnnotationValues = null;

		for (AnnotationNode annotation : (List<AnnotationNode>) method.visibleAnnotations) {
			if (annotation.desc.contains(TEST_ANNOTATION)) {
				testAnnotationValues = annotation.values;
			}
		}

		if (testAnnotationValues == null) {
			// may also be null if the test annotation is present
			return false;
		}

		boolean previousWasEnabledName = false;

		for (Object x : testAnnotationValues) {
			if (previousWasEnabledName) {
				return Boolean.FALSE.equals(x);
			} else if (x.equals(TEST_ANNOTATION_VALUE_ENABLED)) {
				previousWasEnabledName = true;
			}
		}

		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean isExcludeTestClassesWithNonDefaultConstructor() {
		return true;
	}
}
