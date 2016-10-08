package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClassUnderJUnitTest {
	static final String BEFORE_CLASS = "[beforeClass]";
	static final String BEFORE = "[before]";
	static final String TEST_A = "[a]";
	static final String TEST_B = "[b]";
	static final String AFTER = "[after]";
	static final String AFTER_CLASS = "[afterClass]";

	@BeforeClass
	public static void beforeClass() {
		printSyserr(BEFORE_CLASS);
	}

	@Before
	public void before() {
		printSyserr(BEFORE);
	}

	@Test
	public void a() {
		printSyserr(TEST_A);
	}

	@Test
	public void b() {
		printSyserr(TEST_B);
	}

	@After
	public void after() {
		printSyserr(AFTER);
	}

	@AfterClass
	public static void afterClass() {
		printSyserr(AFTER_CLASS);
	}

	private static void printSyserr(String s) {
		System.err.print(s);
	}
}