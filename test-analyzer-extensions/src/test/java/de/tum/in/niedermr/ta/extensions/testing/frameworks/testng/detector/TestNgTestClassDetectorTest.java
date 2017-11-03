package de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.detector;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.sample.SampleClass;

/** Test {@link TestNgTestClassDetector}. */
public class TestNgTestClassDetectorTest {

	private static TestNgTestClassDetector s_detector;

	/** Set up. */
	@BeforeClass
	public static void setUp() {
		s_detector = new TestNgTestClassDetector(false, new String[0], new String[0],
				TestNgTestClassDetectorTest.class.getClassLoader());
	}

	/** Test. */
	@Test
	public void testDetectTestClass() throws IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(TestNgTestClass1.class);
		assertEquals(ClassType.TEST_CLASS, s_detector.analyzeIsTestClass(cn));
	}

	/** Test. */
	@Test
	public void testDetectClassInTestClass() throws IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(TestNgTestClass1.InnerClass.class);
		assertEquals(ClassType.INNER_CLASS_IN_TEST_OR_IGNORED_CLASS, s_detector.analyzeIsTestClass(cn));
	}

	/** Test. */
	@Test
	public void testDetectIgnoredTestClass() throws IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(IgnoredTestNgTestClass2.class);
		assertEquals(ClassType.IGNORED_TEST_CLASS, s_detector.analyzeIsTestClass(cn));
	}

	/** Test. */
	@Test
	public void testDetectNonTestClass() throws IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(SampleClass.class);
		assertEquals(ClassType.NO_TEST_CLASS, s_detector.analyzeIsTestClass(cn));
	}
}
