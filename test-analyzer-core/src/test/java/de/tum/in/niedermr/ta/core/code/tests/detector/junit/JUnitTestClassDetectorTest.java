package de.tum.in.niedermr.ta.core.code.tests.detector.junit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import junit.framework.TestCase;

public class JUnitTestClassDetectorTest {
	private static JUnitTestClassDetector s_detector;

	@BeforeClass
	public static void setUp() {
		s_detector = new JUnitTestClassDetector(false);
	}

	@Test
	public void testDetector() throws IOException {
		ClassNode cn;

		cn = BytecodeUtility.getAcceptedClassNode(JUnit4TestClass.class);
		assertEquals(JUnitClassTypeResult.TEST_CLASS_JUNIT_4, s_detector.analyzeIsTestClass(cn));

		cn = BytecodeUtility.getAcceptedClassNode(JUnit3TestClass.class);
		assertEquals(JUnitClassTypeResult.TEST_CLASS_JUNIT_3, s_detector.analyzeIsTestClass(cn));

		cn = BytecodeUtility.getAcceptedClassNode(NoTestClass1.class);
		assertEquals(ClassType.NO_TEST_CLASS, s_detector.analyzeIsTestClass(cn));

		cn = BytecodeUtility.getAcceptedClassNode(NoTestClass2.class);
		assertEquals(ClassType.NO_TEST_CLASS, s_detector.analyzeIsTestClass(cn));
	}

	@Test
	public void testAbstractTestClasses() throws IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(AbstractJUnit4TestClass.class);

		assertEquals(ClassType.IGNORED_ABSTRACT_CLASS, new JUnitTestClassDetector(false).analyzeIsTestClass(cn));
		assertEquals(JUnitClassTypeResult.TEST_CLASS_JUNIT_4, new JUnitTestClassDetector(true).analyzeIsTestClass(cn));
	}

	@Test
	public void testIgnoredClasses() throws IOException {
		JUnitTestClassDetector detectorWithIgnore = new JUnitTestClassDetector(false,
				"^" + JUnit4TestClass.class.getPackage().getName());

		ClassNode cn = BytecodeUtility.getAcceptedClassNode(JUnit4TestClass.class);
		assertEquals(ClassType.IGNORED_CLASS, detectorWithIgnore.analyzeIsTestClass(cn));
	}

	static class JUnit4TestClass {
		@Test
		public void a() {
			// NOP
		}

		@Test
		@Ignore
		public void ignored() {
			// NOP
		}
	}

	static class JUnit3TestClass extends TestCase {
		public void testA() {
			// NOP
		}
	}

	static abstract class AbstractJUnit4TestClass {
		@Test
		public void a() {
			// NOP
		}
	}

	/**
	 * No annotation, no inheritance.
	 *
	 */
	static class NoTestClass1 {
		public void testA() {
			// NOP
		}
	}

	static class NoTestClass2 {
		@Ignore
		@Test
		public void ignored() {
			// NOP
		}
	}
}
