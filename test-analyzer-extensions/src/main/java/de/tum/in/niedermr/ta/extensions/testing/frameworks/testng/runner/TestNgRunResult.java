package de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.runner;

import java.util.LinkedList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

public class TestNgRunResult implements ITestRunResult {
	private final TestListenerAdapter m_listener;

	public TestNgRunResult(TestListenerAdapter listener) {
		this.m_listener = listener;
	}

	/** {@inheritDoc} */
	@Override
	public boolean successful() {
		return getFailureCount() == 0;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAssertionError() {
		if (successful()) {
			return false;
		}

		return getFirstException() instanceof AssertionError;
	}

	/** {@inheritDoc} */
	@Override
	public int getRunCount() {
		return m_listener.getPassedTests().size() + getFailureCount() + m_listener.getSkippedTests().size();
	}

	/** {@inheritDoc} */
	@Override
	public int getFailureCount() {
		return m_listener.getFailedTests().size();
	}

	/** {@inheritDoc} */
	@Override
	public Throwable getFirstException() {
		if (successful()) {
			return null;
		}

		return m_listener.getFailedTests().get(0).getThrowable();
	}

	/** {@inheritDoc} */
	@Override
	public List<? extends Throwable> getAllExceptions() {
		List<Throwable> result = new LinkedList<>();

		for (ITestResult testResult : m_listener.getFailedTests()) {
			result.add(testResult.getThrowable());
		}

		return result;
	}
}
