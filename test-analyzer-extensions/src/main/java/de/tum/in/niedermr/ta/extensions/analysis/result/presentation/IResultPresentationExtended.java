package de.tum.in.niedermr.ta.extensions.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

/** Result presentation. */
public interface IResultPresentationExtended extends IResultPresentation {

	/** Create an instance and assign the execution id. */
	public static IResultPresentationExtended create(IExecutionId executionId) {
		ExtendedDatabaseResultPresentation resultPresentation = new ExtendedDatabaseResultPresentation();
		resultPresentation.setExecutionId(executionId);
		return resultPresentation;
	}

	/** Format a stack distance information entry. */
	public String formatStackDistanceInfoEntry(TestcaseIdentifier testCaseIdentifier, MethodIdentifier methodUnderTest,
			int minInvocationDistance, int maxInvocationDistance);

	public String formatInstructionsPerMethod(MethodIdentifier methodIdentifier, int instructionCount);

	public String formatModifierPerMethod(MethodIdentifier methodIdentifier, String modifier);

	public String formatInstructionsPerTestcase(MethodIdentifier testcaseIdentifier, int instructionCount);

	public String formatAssertionsPerTestcase(MethodIdentifier testcaseIdentifier, int assertionCount);
}
