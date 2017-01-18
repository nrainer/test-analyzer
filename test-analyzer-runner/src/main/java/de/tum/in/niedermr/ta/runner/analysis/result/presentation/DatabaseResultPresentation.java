package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/**
 * Result presentation that produces SQL statements.
 * 
 * @see "schema.sql"
 */
public class DatabaseResultPresentation implements IResultPresentation {
	/** Pattern to parse the execution id from the execution information. */
	private static final String PATTERN_PARSE_ID_FROM_EXECUTION_INFORMATION = "INSERT INTO Execution_Information \\(execution, .*?\\) VALUES \\('([A-Z0-9]+)',";
	public static final String SQL_INSERT_EXECUTION_INFORMATION = "INSERT INTO Execution_Information (execution, date, project, configurationContent) VALUES ('%s', CURRENT_DATE(), '?', '%s');";
	public static final String SQL_UPDATE_EXECUTION_INFORMATION_WITH_NOTES = "UPDATE Execution_Information SET notes = '%s' WHERE execution = '%s';";
	public static final String SQL_INSERT_METHOD_TEST_CASE_MAPPING = "INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('%s', '%s', '%s');";
	public static final String SQL_INSERT_TEST_RESULT = "INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('%s', '%s', '%s', '%s', %s, %s, '%s');";
	public static final String SQL_INSERT_TEST_ABORT = "INSERT INTO Test_Abort_Import (execution, method, retValGen, cause) VALUES ('%s', '%s', '%s', '%s');";

	private IExecutionId m_executionId;

	/** {@inheritDoc} */
	@Override
	public String formatTestResultEntry(TestcaseIdentifier testcaseIdentifier, ITestRunResult testResult,
			MethodIdentifier mutatedMethod, String returnValueGenerator) {
		return String.format(SQL_INSERT_TEST_RESULT, m_executionId.getShortId(),
				testcaseIdentifier.toMethodIdentifier().get(), mutatedMethod.get(), returnValueGenerator,
				testResult.getFailureCount() > 0, testResult.isAssertionError(), getNameOfFirstException(testResult));
	}

	/** {@inheritDoc} */
	@Override
	public void setExecutionId(IExecutionId execId) {
		this.m_executionId = execId;
	}

	protected IExecutionId getExecutionId() {
		return m_executionId;
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
		return String.format(SQL_INSERT_TEST_ABORT, m_executionId.getShortId(), methodUnderTest.get(),
				returnValueGenerator, abortType.toString());
	}

	/** {@inheritDoc} */
	@Override
	public String formatMethodAndTestcaseMapping(MethodIdentifier methodUnderTest, TestcaseIdentifier testcase) {
		return String.format(SQL_INSERT_METHOD_TEST_CASE_MAPPING, m_executionId.getShortId(), methodUnderTest.get(),
				testcase.toMethodIdentifier().get());
	}

	/** {@inheritDoc} */
	@Override
	public String formatExecutionInformation(List<String> configurationLines) {
		return String.format(SQL_INSERT_EXECUTION_INFORMATION, m_executionId.getShortId(),
				StringUtility.join(configurationLines, CommonConstants.NEW_LINE));
	}

	/** {@inheritDoc} */
	@Override
	public String formatExecutionSummary(String summary) {
		return String.format(SQL_UPDATE_EXECUTION_INFORMATION_WITH_NOTES, summary, m_executionId.getShortId());
	}

	/** {@inheritDoc} */
	@Override
	public String formatLineComment(String comment) {
		return "-- " + comment;
	}

	/** {@inheritDoc} */
	@Override
	public String getBlockCommentStart() {
		return "/*";
	}

	/** {@inheritDoc} */
	@Override
	public String getBlockCommentEnd() {
		return "*/";
	}

	/** Try to parse the execution id from the content of the execution information result file. */
	public static Optional<IExecutionId> tryParseExecutionIdFromExecutionInformation(
			List<String> executionInformationFileContent) {
		if (executionInformationFileContent.isEmpty()) {
			return Optional.empty();
		}

		String firstLine = executionInformationFileContent.get(0);
		Matcher matcher = Pattern.compile(PATTERN_PARSE_ID_FROM_EXECUTION_INFORMATION).matcher(firstLine);

		if (matcher.find()) {
			return Optional.of(ExecutionIdFactory.parseShortExecutionId(matcher.group(1)));
		}

		return Optional.empty();
	}
}
