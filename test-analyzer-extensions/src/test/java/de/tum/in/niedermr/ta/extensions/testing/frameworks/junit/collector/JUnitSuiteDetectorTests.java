package de.tum.in.niedermr.ta.extensions.testing.frameworks.junit.collector;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitClassTypeResult;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.extensions.testing.frameworks.junit.detector.JUnitSuiteDetector;

public class JUnitSuiteDetectorTests {
	@Test
	public void testSuites() throws IOException {
		JUnitSuiteDetector detector = new JUnitSuiteDetector(new String[0], new String[0]);

		ClassNode cn;

		cn = BytecodeUtility.getAcceptedClassNode(JUnit3TestSuite.class);
		assertEquals(JUnitClassTypeResult.TEST_SUITE_JUNIT_3, detector.analyzeIsTestClass(cn));

		cn = BytecodeUtility.getAcceptedClassNode(JUnit4TestSuite.class);
		assertEquals(JUnitClassTypeResult.TEST_SUITE_JUNIT_4, detector.analyzeIsTestClass(cn));

		cn = BytecodeUtility.getAcceptedClassNode(NoTestSuite.class);
		assertEquals(ClassType.NO_TEST_CLASS, detector.analyzeIsTestClass(cn));
	}
}
