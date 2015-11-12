package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

public class JUnitTestRunResult implements ITestRunResult {
	private final Result jUnitResult;

	public JUnitTestRunResult(Result jUnitResult) {
		this.jUnitResult = jUnitResult;
	}

	@Override
	public boolean successful() {
		return jUnitResult.wasSuccessful();
	}

	@Override
	public boolean isAssertionError() {
		if (successful()) {
			return false;
		} else {
			return getFirstException() instanceof java.lang.AssertionError;
		}
	}

	@Override
	public int getRunCount() {
		return jUnitResult.getRunCount();
	}

	@Override
	public int getFailureCount() {
		return jUnitResult.getFailureCount();
	}

	@Override
	public Throwable getFirstException() {
		return successful() ? null : jUnitResult.getFailures().get(0).getException();
	}

	@Override
	public List<? extends Throwable> getAllExceptions() {
		List<Throwable> result = new LinkedList<>();

		for (Failure failure : jUnitResult.getFailures()) {
			result.add(failure.getException());
		}

		return result;
	}
}
