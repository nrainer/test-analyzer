package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortType;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

/**
 * CREATE TABLE IF NOT EXISTS Test_Result (execution VARCHAR(5), testcase VARCHAR(1024), method VARCHAR(1024), retValGen
 * VARCHAR(1024), killed BOOLEAN, assertErr BOOLEAN, exception VARCHAR(256)); CREATE TABLE IF NOT EXISTS Test_Abort
 * (execution VARCHAR(5), method VARCHAR(1024), retValGen VARCHAR(1024), cause VARCHAR(32));
 */
public class DatabaseResultPresentation implements IResultPresentation {
	private static final String SQL_INSERT_TEST_RESULT = "INSERT INTO Test_Result (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('%s', '%s', '%s', '%s', %s, %s, '%s');";
	private static final String SQL_INSERT_TEST_ABORT = "INSERT INTO Test_Abort (execution, method, retValGen, cause) VALUES ('%s', '%s', '%s', '%s');";
	private String m_execId;

	@Override
	public String formatResultInformation(TestcaseIdentifier testcaseIdentifier, ITestRunResult testResult,
			MethodIdentifier mutatedMethod, String returnValueGenerator) {
		return String.format(SQL_INSERT_TEST_RESULT, m_execId, testcaseIdentifier.toMethodIdentifier().get(),
				mutatedMethod.get(), returnValueGenerator, testResult.getFailureCount() > 0,
				testResult.isAssertionError(), getFirstExceptionName(testResult));
	}

	@Override
	public void setShortExecutionId(String execId) {
		this.m_execId = execId;
	}

	private String getFirstExceptionName(ITestRunResult testResult) {
		Throwable t = testResult.getFirstException();

		return t == null ? "" : t.getClass().getName();
	}

	@Override
	public String formatTestAbortInformation(MethodIdentifier methodUnderTest, String returnValueGenerator,
			TestAbortType abortType) {
		return String.format(SQL_INSERT_TEST_ABORT, m_execId, methodUnderTest.get(), returnValueGenerator,
				abortType.toString());
	}
}
