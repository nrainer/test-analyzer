package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

/**
 * Surefire listener with CSV output.
 * 
 * @see AbstractSurefireTestListener
 */
public class SurefireCsvTestListener extends AbstractSurefireTestListener {

	private static final String OUTPUT_FILE_NAME = "./stack-distance.csv";

	/** {@inheritDoc} */
	@Override
	protected String getOutputFileName() {
		return OUTPUT_FILE_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected void writeCommentToResultFile(IResultReceiver resultReceiver, String comment) {
		// not supported
	}

	/** {@inheritDoc} */
	@Override
	protected void appendToResult(IResultReceiver resultReceiver, TestcaseIdentifier testCaseIdentifier,
			MethodIdentifier methodUnderTest, int minInvocationDistance, int invocationCount) {
		String sqlStatement = String.join("|", testCaseIdentifier.toMethodIdentifier().get(), methodUnderTest.get(),
				String.valueOf(minInvocationDistance), String.valueOf(invocationCount));
		resultReceiver.append(sqlStatement);
	}
}
