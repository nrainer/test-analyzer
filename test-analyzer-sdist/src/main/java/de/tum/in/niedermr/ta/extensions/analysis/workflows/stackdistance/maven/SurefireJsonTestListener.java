package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

/**
 * Surefire listener with JSON output for Teamscale.
 * 
 * @see AbstractSurefireTestListener
 */
public class SurefireJsonTestListener extends AbstractSurefireTestListener implements CommonConstants {

	private boolean isFirstEntry = true;

	/** {@inheritDoc} */
	@Override
	protected String getDefaultOutputFileExtension() {
		return ".json";
	}

	/** {@inheritDoc} */
	@Override
	protected void writeCommentToResultFile(IResultReceiver resultReceiver, String comment) {
		// currently not supported
	}

	/** {@inheritDoc} */
	@Override
	protected void execBeforeAllTests(IResultReceiver resultReceiver) {
		resultReceiver.append("[");
	}

	/** {@inheritDoc} */
	@Override
	protected void execAfterAllTests(IResultReceiver resultReceiver) {
		resultReceiver.append("]");
	}

	/** {@inheritDoc} */
	@Override
	protected void appendToResult(IResultReceiver resultReceiver, TestcaseIdentifier testCaseIdentifier,
			MethodIdentifier methodUnderTest, int minInvocationDistance, int invocationCount) {

		if (isFirstEntry) {
			isFirstEntry = false;
		} else {
			resultReceiver.append("  ,");
		}

		resultReceiver.append(toJson(testCaseIdentifier, methodUnderTest, minInvocationDistance, invocationCount));
	}

	private String toJson(TestcaseIdentifier testCaseIdentifier, MethodIdentifier methodUnderTest,
			int minInvocationDistance, int invocationCount) {
		StringBuilder result = new StringBuilder();

		result.append("  {" + NEW_LINE);

		result.append("    " + toJson("testcase", testCaseIdentifier.get()) + "," + NEW_LINE);
		result.append("    " + toJson("method", methodUnderTest.get()) + "," + NEW_LINE);
		result.append("    " + toJson("sdist", String.valueOf(minInvocationDistance)) + "," + NEW_LINE);
		result.append("    " + toJson("invocationCount", String.valueOf(invocationCount)) + NEW_LINE);

		result.append("  }");

		return result.toString();
	}

	protected String toJson(String key, String value) {
		return QUOTATION_MARK + key + QUOTATION_MARK + ": " + QUOTATION_MARK + value + QUOTATION_MARK;
	}
}
