package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClassUnderJUnitTest {
	@BeforeClass
	public static void beforeClass() {
		printSyserr(JUnitTestRunnerTest.BEFORE_CLASS);
	}

	@Before
	public void before() {
		printSyserr(JUnitTestRunnerTest.BEFORE);
	}

	@Test
	public void a() {
		printSyserr(JUnitTestRunnerTest.A);
	}

	@Test
	public void b() {
		printSyserr(JUnitTestRunnerTest.B);
	}

	@After
	public void after() {
		printSyserr(JUnitTestRunnerTest.AFTER);
	}

	@AfterClass
	public static void afterClass() {
		printSyserr(JUnitTestRunnerTest.AFTER_CLASS);
	}

	private static void printSyserr(String s) {
		System.err.print(s);
	}
}