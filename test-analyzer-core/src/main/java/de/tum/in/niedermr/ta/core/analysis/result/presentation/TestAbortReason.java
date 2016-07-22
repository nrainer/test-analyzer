package de.tum.in.niedermr.ta.core.analysis.result.presentation;

/** Reason why a test execution did not complete. */
public enum TestAbortReason {

	/**
	 * The test execution terminated unexpectedly. It is likely that the test invoked a method that invoked
	 * <code>System.exit</code>. It also cannot be excluded that the mutation step created an invalid class file.
	 */
	TEST_DIED,

	/** The test execution was aborted because the timeout was reached. */
	TEST_TIMEOUT
}
