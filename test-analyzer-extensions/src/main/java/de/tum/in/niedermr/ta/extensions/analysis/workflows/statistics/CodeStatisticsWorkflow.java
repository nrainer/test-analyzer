package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.AssertionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.PersistResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.extension.ConfigurationExtensionKey;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

public class CodeStatisticsWorkflow extends AbstractWorkflow {
	public static final ConfigurationExtensionKey COUNT_INSTRUCTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.instructions");
	public static final ConfigurationExtensionKey COUNT_ASSERTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.assertions");

	@Override
	public void start() throws FailedExecution {
		PersistResultStep persistResultStep = new PersistResultStep(super.m_information);

		if (m_information.getConfiguration().getConfigurationExtension().getBooleanValue(COUNT_INSTRUCTIONS)) {
			InstructionCounterStep countInstructionsStep = new InstructionCounterStep(super.m_information);
			countInstructionsStep.run();
			persistResultStep.addResultInstructionsPerMethod(countInstructionsStep.getInstructionsPerMethod());
			persistResultStep.addResultInstructionsPerTestcase(countInstructionsStep.getInstructionsPerTestcase());
		}

		if (m_information.getConfiguration().getConfigurationExtension().getBooleanValue(COUNT_ASSERTIONS)) {
			AssertionCounterStep countAssertionsStep = new AssertionCounterStep(super.m_information);
			countAssertionsStep.run();
			persistResultStep.addResultAssertionsPerTestcase(countAssertionsStep.getAssertionsPerTestcase());
		}

		persistResultStep.run();
	}
}
