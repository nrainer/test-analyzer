package de.tum.in.niedermr.ta.core.code.tests.detector.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetectorTestData.AbstractJUnit4TestClass;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetectorTestData.JUnit3TestClass;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetectorTestData.JUnit4TestClass;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetectorTestData.NoTestClass1;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetectorTestData.NoTestClass2;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;

/** Test {@link JUnitTestClassDetector}. */
public class JUnitTestClassDetectorTest {

	private static final String[] EMPTY_PATTERN_STRINGS = new String[0];
	private static JUnitTestClassDetector s_detector;

	@BeforeClass
	public static void setUp() {
		s_detector = new JUnitTestClassDetector(false, EMPTY_PATTERN_STRINGS, EMPTY_PATTERN_STRINGS);
	}

	/** Test. */
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

	/** Test. */
	@Test
	public void testAbstractTestClasses() throws IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(AbstractJUnit4TestClass.class);

		assertEquals(ClassType.IGNORED_ABSTRACT_CLASS, s_detector.analyzeIsTestClass(cn));
		assertEquals(JUnitClassTypeResult.TEST_CLASS_JUNIT_4,
				new JUnitTestClassDetector(true, EMPTY_PATTERN_STRINGS, EMPTY_PATTERN_STRINGS).analyzeIsTestClass(cn));
	}

	/** Test. */
	/** Test. */
	@Test
	public void testIgnoredClasses() throws IOException {
		JUnitTestClassDetector detectorWithIgnore = new JUnitTestClassDetector(false, EMPTY_PATTERN_STRINGS,
				new String[] { "^" + JUnit4TestClass.class.getPackage().getName() });

		ClassNode cn = BytecodeUtility.getAcceptedClassNode(JUnit4TestClass.class);
		assertEquals(ClassType.IGNORED_CLASS, detectorWithIgnore.analyzeIsTestClass(cn));
	}

	/** Test. */
	@Test
	public void testAnalyzeIsTestcaseForJUnit3() throws IOException {
		ClassNode classNode = BytecodeUtility.getAcceptedClassNode(JUnit3TestClass.class);

		@SuppressWarnings("unchecked")
		List<MethodNode> methods = classNode.methods;

		Optional<MethodNode> methodNodeTestA = methods.stream().filter(methodNode -> methodNode.name.equals("testA"))
				.findAny();
		Optional<MethodNode> methodNodeTestButNoTestcase = methods.stream()
				.filter(methodNode -> methodNode.name.equals("testButNoTestcase")).findAny();

		assertTrue("test data corrupt", methodNodeTestA.isPresent());
		assertTrue("test data corrupt", methodNodeTestButNoTestcase.isPresent());

		assertTrue(s_detector.analyzeIsTestcase(methodNodeTestA.get(), JUnitClassTypeResult.TEST_CLASS_JUNIT_3));
		assertFalse(s_detector.analyzeIsTestcase(methodNodeTestButNoTestcase.get(),
				JUnitClassTypeResult.TEST_CLASS_JUNIT_3));
	}
}
