package de.tum.in.niedermr.ta.core.code.tests.runner;

import java.util.List;

public interface ITestRunResult {
	/**
	 * True, if all test cases were successful / no test failed.
	 */
	public boolean successful();

	/**
	 * True, if an assertion caused the test to fail. False, if an exception thrown by the program caused the test to
	 * fail or all tests passed.
	 */
	public boolean isAssertionError();

	/**
	 * Number of tests that were run.
	 */
	public int getRunCount();

	/**
	 * Number of tests with failure.
	 */
	public int getFailureCount();

	/**
	 * Get the exception of the first failed test or null if all tests passed.
	 */
	public Throwable getFirstException();

	/**
	 * Get all exceptions of all failed tests.
	 */
	public List<? extends Throwable> getAllExceptions();
}
