package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.AssertionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.MethodModifierRetrievalStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.PersistResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.extension.ConfigurationExtensionKey;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

public class CodeStatisticsWorkflow extends AbstractWorkflow {
	public static final ConfigurationExtensionKey COUNT_INSTRUCTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.instructions");
	public static final ConfigurationExtensionKey COUNT_ASSERTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.assertions");
	public static final ConfigurationExtensionKey COLLECT_ACCESS_MODIFIER = ConfigurationExtensionKey
			.create("code.statistics.method.modifier");

	@Override
	public void start() throws FailedExecution {
		PersistResultStep persistResultStep = new PersistResultStep(super.m_context);

		if (m_context.getConfiguration().getConfigurationExtension().getBooleanValue(COUNT_INSTRUCTIONS)) {
			InstructionCounterStep countInstructionsStep = new InstructionCounterStep(super.m_context);
			countInstructionsStep.run();
			persistResultStep.addResultInstructionsPerMethod(countInstructionsStep.getInstructionsPerMethod());
			persistResultStep.addResultInstructionsPerTestcase(countInstructionsStep.getInstructionsPerTestcase());
		}

		if (m_context.getConfiguration().getConfigurationExtension().getBooleanValue(COUNT_ASSERTIONS)) {
			AssertionCounterStep countAssertionsStep = new AssertionCounterStep(super.m_context);
			countAssertionsStep.run();
			persistResultStep.addResultAssertionsPerTestcase(countAssertionsStep.getAssertionsPerTestcase());
		}

		if (m_context.getConfiguration().getConfigurationExtension().getBooleanValue(COLLECT_ACCESS_MODIFIER)) {
			MethodModifierRetrievalStep modifierRetrievalStep = new MethodModifierRetrievalStep(super.m_context);
			modifierRetrievalStep.run();
			persistResultStep.addResultModifierPerMethod(modifierRetrievalStep.getModifierPerMethod());
		}

		persistResultStep.run();
	}
}
