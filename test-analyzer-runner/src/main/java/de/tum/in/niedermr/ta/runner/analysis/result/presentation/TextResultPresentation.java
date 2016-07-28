package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import java.util.List;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

public class TextResultPresentation implements IResultPresentation {

	/** {@inheritDoc} */
	@Override
	public String formatTestResultEntry(TestcaseIdentifier testcaseIdentifier, ITestRunResult testResult,
			MethodIdentifier mutatedMethod, String returnValueGenerator) {
		StringBuilder sB = new StringBuilder();

		sB.append("Testcase: " + testcaseIdentifier.getTestClassName() + "." + testcaseIdentifier.getTestcaseName()
				+ "()");
		sB.append(CommonConstants.NEW_LINE);

		sB.append("Mutated method: " + mutatedMethod.get());
		sB.append(CommonConstants.NEW_LINE);

		sB.append("Return value generator: " + returnValueGenerator);
		sB.append(CommonConstants.NEW_LINE);

		sB.append("Result: " + (testResult.successful() ? "OK"
				: (testResult.getFailureCount() + " of " + testResult.getRunCount() + " FAILED")));
		sB.append(CommonConstants.NEW_LINE);

		for (Throwable ex : testResult.getAllExceptions()) {
			sB.append("Exception: " + ex);
			sB.append(CommonConstants.NEW_LINE);
		}

		sB.append(CommonConstants.SEPARATOR_END_OF_BLOCK);

		return sB.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void setShortExecutionId(String execId) {
		// not needed
	}

	/** {@inheritDoc} */
	@Override
	public String formatTestAbortEntry(MethodIdentifier methodUnderTest, String returnValueGenerator,
			TestAbortReason abortType) {
		StringBuilder sB = new StringBuilder();

		sB.append("Testcases aborted with " + abortType.toString());
		sB.append(CommonConstants.NEW_LINE);

		sB.append("Mutated method: " + methodUnderTest.get());
		sB.append(CommonConstants.NEW_LINE);

		sB.append("Return value generator: " + returnValueGenerator);
		sB.append(CommonConstants.NEW_LINE);

		sB.append("Result: " + abortType.toString());
		sB.append(CommonConstants.NEW_LINE);

		sB.append(CommonConstants.SEPARATOR_END_OF_BLOCK);

		return sB.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String formatMethodAndTestcaseMapping(MethodIdentifier methodUnderTest, TestcaseIdentifier testcase) {
		StringBuilder sB = new StringBuilder();

		sB.append("Method: " + methodUnderTest.get());
		sB.append(CommonConstants.NEW_LINE);

		sB.append("Tested by: " + testcase.toMethodIdentifier().get());
		sB.append(CommonConstants.NEW_LINE);

		sB.append(CommonConstants.SEPARATOR_END_OF_BLOCK);

		return sB.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String formatExecutionInformation(List<String> configurationLines) {
		return configurationLines.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String formatExecutionSummary(String summary) {
		return summary;
	}
}
