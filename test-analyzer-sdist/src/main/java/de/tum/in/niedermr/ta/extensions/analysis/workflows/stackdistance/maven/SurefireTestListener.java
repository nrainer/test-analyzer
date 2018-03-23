package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

/**
 * Surefire listener with SQL output.
 * 
 * @see AbstractSurefireTestListener
 */
public class SurefireTestListener extends AbstractSurefireTestListener {

	private static final String OUTPUT_FILE_NAME = "./stack-distance.sql";

	/**
	 * Note that for parameterized test cases more than one statement may be created for a certain test case identifier.
	 */
	private static final String SQL_INSERT_STACK_DISTANCE_STATEMENT = "INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, invocationCount)"
			+ " VALUES ('%s', '%s', '%s', '%s', '%s'); ";
	private static final String DEFAULT_EXECUTION_ID = "????";

	/** {@inheritDoc} */
	@Override
	protected String getOutputFileName() {
		return OUTPUT_FILE_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected void writeCommentToResultFile(IResultReceiver resultReceiver, String comment) {
		resultReceiver.append("#" + comment);
	}

	/** {@inheritDoc} */
	@Override
	protected void appendToResult(IResultReceiver resultReceiver, TestcaseIdentifier testCaseIdentifier,
			MethodIdentifier methodUnderTest, int minInvocationDistance, int invocationCount) {
		String sqlStatement = String.format(SQL_INSERT_STACK_DISTANCE_STATEMENT, DEFAULT_EXECUTION_ID,
				testCaseIdentifier, methodUnderTest, minInvocationDistance, invocationCount);
		resultReceiver.append(sqlStatement);
	}
}
