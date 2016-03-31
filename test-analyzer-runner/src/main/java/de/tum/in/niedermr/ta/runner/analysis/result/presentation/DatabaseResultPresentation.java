package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

/**
 * <code>
 * CREATE TABLE IF NOT EXISTS Collected_Information (execution VARCHAR(5), method VARCHAR(1024), testcase VARCHAR(1024)); <br/>
 * CREATE TABLE IF NOT EXISTS Test_Result (execution VARCHAR(5), testcase VARCHAR(1024), method VARCHAR(1024), retValGen
 * VARCHAR(1024), killed BOOLEAN, assertErr BOOLEAN, exception VARCHAR(256));<br/>
 * CREATE TABLE IF NOT EXISTS Test_Abort (execution VARCHAR(5), method VARCHAR(1024), retValGen VARCHAR(1024), cause VARCHAR(32));
 * </code>
 */
public class DatabaseResultPresentation implements IResultPresentation {
	public static final String SQL_INSERT_METHOD_TEST_CASE_MAPPING = "INSERT INTO Collected_Information (execution, method, testcase) VALUES ('%s', '%s', '%s');";
	public static final String SQL_INSERT_TEST_RESULT = "INSERT INTO Test_Result (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('%s', '%s', '%s', '%s', %s, %s, '%s');";
	public static final String SQL_INSERT_TEST_ABORT = "INSERT INTO Test_Abort (execution, method, retValGen, cause) VALUES ('%s', '%s', '%s', '%s');";

	private String m_executionId;

	/** {@inheritDoc} */
	@Override
	public String formatTestResultEntry(TestcaseIdentifier testcaseIdentifier, ITestRunResult testResult,
			MethodIdentifier mutatedMethod, String returnValueGenerator) {
		return String.format(SQL_INSERT_TEST_RESULT, m_executionId, testcaseIdentifier.toMethodIdentifier().get(),
				mutatedMethod.get(), returnValueGenerator, testResult.getFailureCount() > 0,
				testResult.isAssertionError(), getNameOfFirstException(testResult));
	}

	/** {@inheritDoc} */
	@Override
	public void setShortExecutionId(String execId) {
		this.m_executionId = execId;
	}

	/** Get the name of the first test exception. */
	private String getNameOfFirstException(ITestRunResult testResult) {
		Throwable throwable = testResult.getFirstException();

		if (throwable != null) {
			return throwable.getClass().getName();
		}

		return "";
	}

	/** {@inheritDoc} */
	@Override
	public String formatTestAbortEntry(MethodIdentifier methodUnderTest, String returnValueGenerator,
			TestAbortReason abortType) {
		return String.format(SQL_INSERT_TEST_ABORT, m_executionId, methodUnderTest.get(), returnValueGenerator,
				abortType.toString());
	}

	/** {@inheritDoc} */
	@Override
	public String formatMethodAndTestcaseMapping(MethodIdentifier methodUnderTest, TestcaseIdentifier testcase) {
		return String.format(SQL_INSERT_METHOD_TEST_CASE_MAPPING, m_executionId, methodUnderTest.get(),
				testcase.toMethodIdentifier().get());
	}
}
