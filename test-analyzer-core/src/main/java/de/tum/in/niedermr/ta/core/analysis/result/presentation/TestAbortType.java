package de.tum.in.niedermr.ta.core.analysis.result.presentation;

public enum TestAbortType {

	/**
	 * The test execution terminated unexpectedly. It is likely that the test invoked a method that invoked
	 * <code>System.exit</code>. It also cannot be excluded that the mutation step created an invalid class file.
	 */
	TEST_DIED,
	/** The test execution was aborted because the timeout was reached. */
	TEST_TIMEOUT
}
