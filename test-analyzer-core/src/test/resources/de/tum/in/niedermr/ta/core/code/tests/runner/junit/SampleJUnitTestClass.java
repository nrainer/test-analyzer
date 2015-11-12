package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SampleJUnitTestClass {
	/**
	 * Supposed to succeed.
	 */
	@Test
	public void a() {
		assertTrue(true);
	}

	/**
	 * Supposed to fail due to an assertion error.
	 */
	@Test
	public void b() {
		assertTrue(false);
	}

	/**
	 * Supposed to fail due to an exception.
	 */
	@Test
	public void c() {
		throw new IllegalStateException();
	}
}