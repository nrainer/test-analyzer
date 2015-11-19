package de.tum.in.niedermr.ta.core.code.tests.detector.junit;

import java.util.List;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.AbstractTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class JUnitTestClassDetector extends AbstractTestClassDetector {
	private static final String JUNIT_3_TEST_METHOD_NAME_PREFIX = "test";
	private static final String JUNIT_4_TEST_ANNOTATION = "Lorg/junit/Test;";
	private static final String JUNIT_4_IGNORE_ANNOTATION = "Lorg/junit/Ignore;";

	public JUnitTestClassDetector(boolean acceptAbstractTestClasses, String[] testClassIncludes,
			String[] testClassExcludes) {
		super(acceptAbstractTestClasses, testClassIncludes, testClassExcludes);
	}

	@Override
	protected ClassType isTestClassInternal(ClassNode cn) {
		if (isJUnit3TestClass(cn)) {
			return JUnitClassTypeResult.TEST_CLASS_JUNIT_3;
		} else if (isJUnit4TestClass(cn)) {
			return JUnitClassTypeResult.TEST_CLASS_JUNIT_4;
		} else {
			return ClassType.NO_TEST_CLASS;
		}
	}

	private boolean isJUnit3TestClass(ClassNode cn) {
		return JavaUtility.inheritsClassNoEx(cn, junit.framework.TestCase.class, true);
	}

	@SuppressWarnings("unchecked")
	private boolean isJUnit4TestClass(ClassNode cn) {
		for (MethodNode method : (List<MethodNode>) cn.methods) {
			if (isJUnit4Testcase(method)) {
				return true;
			}
		}

		return false;
	}

	private boolean isJUnit3Testcase(MethodNode method) {
		return method.name.startsWith(JUNIT_3_TEST_METHOD_NAME_PREFIX);
	}

	@SuppressWarnings("unchecked")
	private boolean isJUnit4Testcase(MethodNode method) {
		if (method.visibleAnnotations == null) {
			return false;
		}

		boolean isTestCase = false;
		boolean isIgnored = false;

		for (AnnotationNode annotation : (List<AnnotationNode>) method.visibleAnnotations) {
			if (annotation.desc.equals(JUNIT_4_TEST_ANNOTATION)) {
				isTestCase = true;
			} else if (annotation.desc.equals(JUNIT_4_IGNORE_ANNOTATION)) {
				isIgnored = true;
			}
		}

		return IGNORE_IGNORED_TEST_CASES ? (isTestCase && !isIgnored) : isTestCase;
	}

	@Override
	public boolean analyzeIsTestcase(MethodNode methodNode, ClassType testClassType) {
		return (testClassType == JUnitClassTypeResult.TEST_CLASS_JUNIT_3 && isJUnit3Testcase(methodNode))
				|| (testClassType == JUnitClassTypeResult.TEST_CLASS_JUNIT_4 && isJUnit4Testcase(methodNode));
	}
}
