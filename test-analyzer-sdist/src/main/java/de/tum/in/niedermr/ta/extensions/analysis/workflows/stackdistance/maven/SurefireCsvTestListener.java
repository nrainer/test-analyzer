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

	/** Separator: do not use <code>;</code> because it can appear in method signatures. */
	private static final String CSV_SEPARATOR = "|";

	/** {@inheritDoc} */
	@Override
	protected String getDefaultOutputFileExtension() {
		return ".csv";
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
		String sqlStatement = String.join(CSV_SEPARATOR, testCaseIdentifier.toMethodIdentifier().get(),
				methodUnderTest.get(), String.valueOf(minInvocationDistance), String.valueOf(invocationCount));
		resultReceiver.append(sqlStatement);
	}
}
