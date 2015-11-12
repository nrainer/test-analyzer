package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tum.in.niedermr.ta.sample.junit.SampleJUnitTestClass;

public class JUnitTestRunResultTest {
	private static final JUnitTestRunner TEST_RUNNER = new JUnitTestRunner();

	@Test
	public void test1() {
		JUnitTestRunResult result = TEST_RUNNER.runTest(SampleJUnitTestClass.class, "a");

		assertTrue(result.successful());
		assertEquals(1, result.getRunCount());
		assertEquals(0, result.getFailureCount());
		assertNull(result.getFirstException());
		assertTrue(result.getAllExceptions().isEmpty());
	}

	@Test
	public void test2() {
		JUnitTestRunResult result = TEST_RUNNER.runTest(SampleJUnitTestClass.class, "b");

		assertFalse(result.successful());
		assertEquals(1, result.getRunCount());
		assertEquals(1, result.getFailureCount());
		assertTrue(result.isAssertionError());
		assertNotNull(result.getFirstException());
		assertEquals(1, result.getAllExceptions().size());
		assertEquals(result.getFirstException(), result.getAllExceptions().get(0));
	}

	@Test
	public void test3() {
		JUnitTestRunResult result = TEST_RUNNER.runTest(SampleJUnitTestClass.class, "c");

		assertFalse(result.successful());
		assertEquals(1, result.getRunCount());
		assertEquals(1, result.getFailureCount());
		assertFalse(result.isAssertionError());
		assertEquals(IllegalStateException.class, result.getFirstException().getClass());
		assertEquals(1, result.getAllExceptions().size());
		assertEquals(result.getFirstException(), result.getAllExceptions().get(0));
	}
}
