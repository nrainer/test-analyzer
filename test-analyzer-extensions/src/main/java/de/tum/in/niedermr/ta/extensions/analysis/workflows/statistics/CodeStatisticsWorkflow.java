package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.AssertionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.MethodModifierRetrievalStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.PersistResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class CodeStatisticsWorkflow extends AbstractWorkflow {
	/** <code>extension.code.statistics.method.instructions</code> */
	public static final DynamicConfigurationKey COUNT_INSTRUCTIONS = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "code.statistics.method.instructions", true);
	/** <code>extension.code.statistics.method.assertions</code> */
	public static final DynamicConfigurationKey COUNT_ASSERTIONS = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "code.statistics.method.assertions", true);
	/** <code>extension.code.statistics.method.modifier</code> */
	public static final DynamicConfigurationKey COLLECT_ACCESS_MODIFIER = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "code.statistics.method.modifier", true);

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();

		PersistResultStep persistResultStep = createAndInitializeExecutionStep(PersistResultStep.class);

		if (configuration.getDynamicValues().getBooleanValue(COUNT_INSTRUCTIONS)) {
			runCountInstructionsStep(persistResultStep);
		}

		if (configuration.getDynamicValues().getBooleanValue(COUNT_ASSERTIONS)) {
			runCountAssertionsStep(persistResultStep);
		}

		if (configuration.getDynamicValues().getBooleanValue(COLLECT_ACCESS_MODIFIER)) {
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
