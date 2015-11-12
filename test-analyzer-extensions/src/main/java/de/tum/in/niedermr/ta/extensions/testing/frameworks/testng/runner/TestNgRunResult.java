package de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.runner;

import java.util.LinkedList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

public class TestNgRunResult implements ITestRunResult {
	private final TestListenerAdapter listener;

	public TestNgRunResult(TestListenerAdapter listener) {
		this.listener = listener;
	}

	@Override
	public boolean successful() {
		return getFailureCount() == 0;
	}

	@Override
	public boolean isAssertionError() {
		if (successful()) {
			return false;
		} else {
			return getFirstException() instanceof AssertionError;
		}
	}

	@Override
	public int getRunCount() {
		return listener.getPassedTests().size() + getFailureCount() + listener.getSkippedTests().size();
	}

	@Override
	public int getFailureCount() {
		return listener.getFailedTests().size();
	}

	@Override
	public Throwable getFirstException() {
		return successful() ? null : listener.getFailedTests().get(0).getThrowable();
	}

	@Override
	public List<? extends Throwable> getAllExceptions() {
		List<Throwable> result = new LinkedList<>();

		for (ITestResult testResult : listener.getFailedTests()) {
			result.add(testResult.getThrowable());
		}

		return result;
	}
}
