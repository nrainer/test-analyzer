package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JUnitTestRunnerTest {

	private final JUnitTestRunner m_testRunner = new JUnitTestRunner();
	private PrintStream m_originalSysErr;
	private ByteArrayOutputStream m_temporarySysErr;

	@Before
	public void before() {
		this.m_originalSysErr = System.err;
		this.m_temporarySysErr = new ByteArrayOutputStream();
		System.setErr(new PrintStream(m_temporarySysErr));
	}

	@After
	public void after() {
		System.setErr(m_originalSysErr);
	}

	private String getSysErr() throws IOException {
		m_temporarySysErr.flush();
		return m_temporarySysErr.toString();
	}

	@Test
	public void testRunTest() throws IOException {
		m_testRunner.runTest(ClassUnderJUnitTest.class, "a");

		assertEquals(ClassUnderJUnitTest.BEFORE_CLASS + ClassUnderJUnitTest.BEFORE + ClassUnderJUnitTest.TEST_A
				+ ClassUnderJUnitTest.AFTER + ClassUnderJUnitTest.AFTER_CLASS, getSysErr());
	}

	@Test
	public void testRunTestsWithoutResult() throws IOException {
		m_testRunner.runTestsWithoutResult(ClassUnderJUnitTest.class);

		String sysErr = getSysErr();

		final String beginning = ClassUnderJUnitTest.BEFORE_CLASS + ClassUnderJUnitTest.BEFORE;
		final String ending = ClassUnderJUnitTest.AFTER + ClassUnderJUnitTest.AFTER_CLASS;

		assertTrue(sysErr
				.equals(beginning + ClassUnderJUnitTest.TEST_A + ClassUnderJUnitTest.AFTER + ClassUnderJUnitTest.BEFORE
						+ ClassUnderJUnitTest.TEST_B + ending)
				|| sysErr.equals(beginning + ClassUnderJUnitTest.TEST_B + ClassUnderJUnitTest.AFTER
						+ ClassUnderJUnitTest.BEFORE + ClassUnderJUnitTest.TEST_A + ending));
	}
}
