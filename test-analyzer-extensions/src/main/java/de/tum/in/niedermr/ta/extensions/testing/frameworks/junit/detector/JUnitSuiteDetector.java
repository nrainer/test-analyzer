package de.tum.in.niedermr.ta.extensions.testing.frameworks.junit.detector;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.AbstractTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitClassTypeResult;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class JUnitSuiteDetector extends AbstractTestClassDetector {
	private static final String SUITE_METHOD_NAME = "suite";
	private static final String JUNIT_4_RUN_WITH_ANNOTATION = "Lorg/junit/runner/RunWith;";
	private static final String JUNIT_4_SUITE_CLASSES_ANNOTATION = "Lorg/junit/runners/Suite$SuiteClasses;";

	public JUnitSuiteDetector(String[] testClassIncludes, String[] testClassExcludes) {
		// accept abstract classes always since no instance is needed (to invoke the static suite method) and inherited
		// suite methods are not considered
		super(true, testClassIncludes, testClassExcludes);
	}

	@Override
	protected ClassType isTestClassInternal(ClassNode cn) {
		if (isJUnit3TestSuite(cn)) {
			return JUnitClassTypeResult.TEST_SUITE_JUNIT_3;
		} else if (isJUnit4TestSuite(cn)) {
			return JUnitClassTypeResult.TEST_SUITE_JUNIT_4;
		} else {
			return ClassType.NO_TEST_CLASS;
		}
	}

	private boolean isJUnit3TestSuite(ClassNode cn) {
		try {
			Class<?> cls = Class.forName(JavaUtility.toClassName(cn.name));
			return getJUnit3SuiteMethod(cls) != null;
		} catch (ClassNotFoundException ex) {
			// should not occur
			ex.printStackTrace();
			return false;
		}
	}

	public Method getJUnit3SuiteMethod(Class<?> cls) {
		try {
			Method suiteMethod = cls.getDeclaredMethod(SUITE_METHOD_NAME);

			if (Modifier.isStatic(suiteMethod.getModifiers())
					&& JavaUtility.inheritsClass(suiteMethod.getReturnType(), junit.framework.Test.class)) {
				return suiteMethod;
			} else {
				return null;
			}
		} catch (NoSuchMethodException | SecurityException ex) {
			// not a suite class
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private boolean isJUnit4TestSuite(ClassNode cn) {
		if (cn.visibleAnnotations == null) {
			return false;
		}

		boolean annotatedRunWith = false;
		boolean annotatedSuiteClasses = false;

		for (AnnotationNode annotation : (List<AnnotationNode>) cn.visibleAnnotations) {
			if (annotation.desc.equals(JUNIT_4_RUN_WITH_ANNOTATION)) {
				annotatedRunWith = true;
			} else if (annotation.desc.equals(JUNIT_4_SUITE_CLASSES_ANNOTATION)) {
				annotatedSuiteClasses = true;
			}
		}

		return annotatedRunWith && annotatedSuiteClasses;
	}

	@Override
	public boolean analyzeIsTestcase(MethodNode methodNode, ClassType testClassType)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
