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
	static final String BEFORE_CLASS = "[beforeClass]";
	static final String BEFORE = "[before]";
	static final String A = "[a]";
	static final String B = "[b]";
	static final String AFTER = "[after]";
	static final String AFTER_CLASS = "[afterClass]";

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

		assertEquals(BEFORE_CLASS + BEFORE + A + AFTER + AFTER_CLASS, getSysErr());
	}

	@Test
	public void testRunTestsWithoutResult() throws IOException {
		m_testRunner.runTestsWithoutResult(ClassUnderJUnitTest.class);

		String sysErr = getSysErr();

		final String beginning = BEFORE_CLASS + BEFORE;
		final String ending = AFTER + AFTER_CLASS;

		assertTrue(sysErr.equals(beginning + A + AFTER + BEFORE + B + ending) || sysErr.equals(beginning + B + AFTER + BEFORE + A + ending));
	}
}
