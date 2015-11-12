package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.AssertionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.PersistResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

public class CodeStatisticsWorkflow extends AbstractWorkflow {
	@Override
	public void start() throws FailedExecution {
		InstructionCounterStep countInstructionsStep = new InstructionCounterStep(super.information);
		AssertionCounterStep countAssertionsStep = new AssertionCounterStep(super.information);
		PersistResultStep persistResultStep = new PersistResultStep(super.information);

		countInstructionsStep.run();
		countAssertionsStep.run();

		persistResultStep.addResultInstructionsPerMethod(countInstructionsStep.getInstructionsPerMethod());
		persistResultStep.addResultInstructionsPerTestcase(countInstructionsStep.getInstructionsPerTestcase());
		persistResultStep.addResultAssertionsPerTestcase(countAssertionsStep.getAssertionsPerTestcase());

		persistResultStep.run();
	}
}
