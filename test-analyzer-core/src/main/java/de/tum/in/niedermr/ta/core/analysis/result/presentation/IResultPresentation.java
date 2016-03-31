package de.tum.in.niedermr.ta.core.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

/** Format the result of the workflow execution. */
public interface IResultPresentation {

	/** Set the execution id to identify the workflow execution. */
	void setShortExecutionId(String execId);

	/** Format an entry about a completed test execution. */
	String formatTestResultEntry(TestcaseIdentifier testcaseIdentifier, ITestRunResult testResult,
			MethodIdentifier methodUnderTest, String returnValueGeneratorName);

	/** Format an entry about an aborted test execution. */
	String formatTestAbortEntry(MethodIdentifier methodUnderTest, String returnValueGeneratorName,
			TestAbortReason abortType);
}
