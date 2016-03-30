package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.AssertionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.MethodModifierRetrievalStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.PersistResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.ConfigurationExtensionKey;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class CodeStatisticsWorkflow extends AbstractWorkflow {
	public static final ConfigurationExtensionKey COUNT_INSTRUCTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.instructions");
	public static final ConfigurationExtensionKey COUNT_ASSERTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.assertions");
	public static final ConfigurationExtensionKey COLLECT_ACCESS_MODIFIER = ConfigurationExtensionKey
			.create("code.statistics.method.modifier");

	@Override
	public void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PersistResultStep persistResultStep = createAndInitializeExecutionStep(PersistResultStep.class);

		if (configuration.getExtension().getBooleanValue(COUNT_INSTRUCTIONS)) {
			InstructionCounterStep countInstructionsStep = createAndInitializeExecutionStep(
					InstructionCounterStep.class);
			countInstructionsStep.run();
			persistResultStep.addResultInstructionsPerMethod(countInstructionsStep.getInstructionsPerMethod());
			persistResultStep.addResultInstructionsPerTestcase(countInstructionsStep.getInstructionsPerTestcase());
		}

		if (configuration.getExtension().getBooleanValue(COUNT_ASSERTIONS)) {
			AssertionCounterStep countAssertionsStep = createAndInitializeExecutionStep(AssertionCounterStep.class);
			countAssertionsStep.run();
			persistResultStep.addResultAssertionsPerTestcase(countAssertionsStep.getAssertionsPerTestcase());
		}

		if (configuration.getExtension().getBooleanValue(COLLECT_ACCESS_MODIFIER)) {
			MethodModifierRetrievalStep modifierRetrievalStep = createAndInitializeExecutionStep(
					MethodModifierRetrievalStep.class);
			modifierRetrievalStep.run();
			persistResultStep.addResultModifierPerMethod(modifierRetrievalStep.getModifierPerMethod());
		}

		persistResultStep.run();
	}
}
