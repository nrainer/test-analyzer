package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.AssertionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.MethodModifierRetrievalStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.PersistResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.ConfigurationExtensionKey;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class CodeStatisticsWorkflow extends AbstractWorkflow {
	/** <code>extension.code.statistics.method.instructions</code> */
	public static final ConfigurationExtensionKey COUNT_INSTRUCTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.instructions");
	/** <code>extension.code.statistics.method.assertions</code> */
	public static final ConfigurationExtensionKey COUNT_ASSERTIONS = ConfigurationExtensionKey
			.create("code.statistics.method.assertions");
	/** <code>extension.code.statistics.method.modifier</code> */
	public static final ConfigurationExtensionKey COLLECT_ACCESS_MODIFIER = ConfigurationExtensionKey
			.create("code.statistics.method.modifier");

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();

		PersistResultStep persistResultStep = createAndInitializeExecutionStep(PersistResultStep.class);

		if (configuration.getExtension().getBooleanValue(COUNT_INSTRUCTIONS, true)) {
			runCountInstructionsStep(persistResultStep);
		}

		if (configuration.getExtension().getBooleanValue(COUNT_ASSERTIONS, true)) {
			runCountAssertionsStep(persistResultStep);
		}

		if (configuration.getExtension().getBooleanValue(COLLECT_ACCESS_MODIFIER, true)) {
			runCollectAccessModifiersStep(persistResultStep);
		}

		persistResultStep.start();
	}

	/** Run the step to count the instructions of methods and test cases. */
	protected void runCountInstructionsStep(PersistResultStep persistResultStep) {
		InstructionCounterStep countInstructionsStep = createAndInitializeExecutionStep(InstructionCounterStep.class);
		countInstructionsStep.start();
		persistResultStep.addResultInstructionsPerMethod(countInstructionsStep.getInstructionsPerMethod());
		persistResultStep.addResultInstructionsPerTestcase(countInstructionsStep.getInstructionsPerTestcase());
	}

	/** Run the step to count assertions. */
	protected void runCountAssertionsStep(PersistResultStep persistResultStep) {
		AssertionCounterStep countAssertionsStep = createAndInitializeExecutionStep(AssertionCounterStep.class);
		countAssertionsStep.start();
		persistResultStep.addResultAssertionsPerTestcase(countAssertionsStep.getAssertionsPerTestcase());
	}

	/** Run the step to collect the access modifiers of methods. */
	protected void runCollectAccessModifiersStep(PersistResultStep persistResultStep) {
		MethodModifierRetrievalStep modifierRetrievalStep = createAndInitializeExecutionStep(
				MethodModifierRetrievalStep.class);
		modifierRetrievalStep.start();
		persistResultStep.addResultModifierPerMethod(modifierRetrievalStep.getModifierPerMethod());
	}
}
