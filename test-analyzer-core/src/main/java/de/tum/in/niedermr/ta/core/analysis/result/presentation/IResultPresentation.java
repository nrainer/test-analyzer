package de.tum.in.niedermr.ta.core.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;

public interface IResultPresentation {
	String formatResultInformation(TestcaseIdentifier testcaseIdentifier, ITestRunResult testResult, MethodIdentifier methodUnderTest, String returnValueGenerator);

	void setShortExecutionId(String execId);
}
