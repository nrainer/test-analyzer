package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.result.ResultReceiverForCodeStatistics;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.AssertionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.MethodModifierRetrievalStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class CodeStatisticsWorkflow extends AbstractWorkflow {

	/**
	 * <code>extension.code.statistics.useMultipleOutputFiles</code>: Split the output into multiple files.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "code.statistics.useMultipleOutputFiles", false);

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

		ResultReceiverForCodeStatistics resultReceiver = createResultReceiverForCodeStatistics(context);

		if (configuration.getDynamicValues().getBooleanValue(COUNT_INSTRUCTIONS)) {
			runCountInstructionsStep(resultReceiver);
		}

		if (configuration.getDynamicValues().getBooleanValue(COUNT_ASSERTIONS)) {
			runCountAssertionsStep(resultReceiver);
		}

		if (configuration.getDynamicValues().getBooleanValue(COLLECT_ACCESS_MODIFIER)) {
			runCollectAccessModifiersStep(resultReceiver);
		}

		resultReceiver.markResultAsComplete();
	}

	protected ResultReceiverForCodeStatistics createResultReceiverForCodeStatistics(ExecutionContext context) {
		IResultPresentationExtended resultPresentationExtended = IResultPresentationExtended
				.create(context.getExecutionId());

		boolean useMultipleOutputFiles = context.getConfiguration().getDynamicValues()
				.getBooleanValue(CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES);
		String resultFileName = getFileInWorkingArea(context,
				ExtensionEnvironmentConstants.FILE_OUTPUT_CODE_STATISTICS);
		IResultReceiver resultReceiver = ResultReceiverFactory
				.createFileResultReceiverWithDefaultSettings(useMultipleOutputFiles, resultFileName);

		return new ResultReceiverForCodeStatistics(resultReceiver, resultPresentationExtended);
	}

	/** Run the step to count the instructions of methods and test cases. */
	protected void runCountInstructionsStep(ResultReceiverForCodeStatistics resultReceiver) {
		InstructionCounterStep countInstructionsStep = createAndInitializeExecutionStep(InstructionCounterStep.class);
		countInstructionsStep.start();

		resultReceiver.addResultInstructionsPerMethod(countInstructionsStep.getInstructionsPerMethod());
		resultReceiver.markResultAsPartiallyComplete();
		resultReceiver.addResultInstructionsPerTestcase(countInstructionsStep.getInstructionsPerTestcase());
		resultReceiver.markResultAsPartiallyComplete();
	}

	/** Run the step to count assertions. */
	protected void runCountAssertionsStep(ResultReceiverForCodeStatistics resultReceiver) {
		AssertionCounterStep countAssertionsStep = createAndInitializeExecutionStep(AssertionCounterStep.class);
		countAssertionsStep.start();

		resultReceiver.addResultAssertionsPerTestcase(countAssertionsStep.getAssertionsPerTestcase());
		resultReceiver.markResultAsPartiallyComplete();
	}

	/** Run the step to collect the access modifiers of methods. */
	protected void runCollectAccessModifiersStep(ResultReceiverForCodeStatistics resultReceiver) {
		MethodModifierRetrievalStep modifierRetrievalStep = createAndInitializeExecutionStep(
				MethodModifierRetrievalStep.class);
		modifierRetrievalStep.start();

		resultReceiver.addResultModifierPerMethod(modifierRetrievalStep.getModifierPerMethod());
		resultReceiver.markResultAsPartiallyComplete();
	}
}
