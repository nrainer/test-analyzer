package de.tum.in.niedermr.ta.core.analysis.result.presentation;

import java.util.List;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

/** Format the result of the workflow execution. */
public interface IResultPresentation {

	/** Set the execution id to identify the workflow execution. */
	void setExecutionId(IExecutionId executionId);

	/** Format an entry about a completed test execution. */
	String formatTestResultEntry(TestcaseIdentifier testcaseIdentifier, ITestRunResult testResult,
			MethodIdentifier methodUnderTest, String returnValueGeneratorName);

	/** Format an entry about an aborted test execution. */
	String formatTestAbortEntry(MethodIdentifier methodUnderTest, String returnValueGeneratorName,
			TestAbortReason abortType);

	/** Format an entry about a method-testcase-mapping. */
	String formatMethodAndTestcaseMapping(MethodIdentifier methodUnderTest, TestcaseIdentifier testcase);

	/** Format the execution information. */
	String formatExecutionInformation(List<String> configurationLines);

	/** Format the execution information summary. */
	String formatExecutionSummary(String summary);
}
