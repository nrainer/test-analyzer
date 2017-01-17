package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

public class JUnitTestRunResult implements ITestRunResult {
	private final Result m_jUnitResult;

	public JUnitTestRunResult(Result jUnitResult) {
		this.m_jUnitResult = jUnitResult;
	}

	/** {@inheritDoc} */
	@Override
	public boolean successful() {
		return m_jUnitResult.wasSuccessful();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAssertionError() {
		if (successful()) {
			return false;
		}
		return getFirstException() instanceof java.lang.AssertionError;
	}

	/** {@inheritDoc} */
	@Override
	public int getRunCount() {
		return m_jUnitResult.getRunCount();
	}

	/** {@inheritDoc} */
	@Override
	public int getFailureCount() {
		return m_jUnitResult.getFailureCount();
	}

	/** {@inheritDoc} */
	@Override
	public Throwable getFirstException() {
		if (successful()) {
			return null;
		}

		return m_jUnitResult.getFailures().get(0).getException();
	}

	/** {@inheritDoc} */
	@Override
	public List<? extends Throwable> getAllExceptions() {
		List<Throwable> result = new LinkedList<>();

		for (Failure failure : m_jUnitResult.getFailures()) {
			result.add(failure.getException());
		}

		return result;
	}
}
