package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import java.util.List;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;

/**
 * Result presentation that produces SQL statements.
 * 
 * @see "schema.sql"
 */
public class DatabaseResultPresentation implements IResultPresentation {
	public static final String SQL_INSERT_EXECUTION_INFORMATION = "INSERT INTO Execution_Information (execution, date, project, configurationContent) VALUES ('%s', CURRENT_DATE(), '?', '%s');";
	public static final String SQL_UPDATE_EXECUTION_INFORMATION_WITH_NOTES = "UPDATE Execution_Information SET notes = '%s' WHERE execution = '%s';";
	public static final String SQL_INSERT_METHOD_TEST_CASE_MAPPING = "INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('%s', '%s', '%s');";
	public static final String SQL_INSERT_TEST_RESULT = "INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('%s', '%s', '%s', '%s', %s, %s, '%s');";
	public static final String SQL_INSERT_TEST_ABORT = "INSERT INTO Test_Abort_Import (execution, method, retValGen, cause) VALUES ('%s', '%s', '%s', '%s');";

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

	/** {@inheritDoc} */
	@Override
	public String formatExecutionInformation(List<String> configurationLines) {
		return String.format(SQL_INSERT_EXECUTION_INFORMATION, m_executionId,
				StringUtility.join(configurationLines, CommonConstants.NEW_LINE));
	}

	@Override
	public String formatExecutionSummary(String summary) {
		return String.format(SQL_UPDATE_EXECUTION_INFORMATION_WITH_NOTES, summary, m_executionId);
	}
}
