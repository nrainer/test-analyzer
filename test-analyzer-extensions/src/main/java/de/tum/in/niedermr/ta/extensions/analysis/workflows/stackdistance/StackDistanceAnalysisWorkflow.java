package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInformationCollectorStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInstrumentationStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.CleanupStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * Computes the minimum and maximum distance on the call stack between test case
 * and method.
 */
public class StackDistanceAnalysisWorkflow extends AbstractWorkflow {

	/**
	 * <code>extension.stackdistance.useMultipleOutputFiles</code>: Split the
	 * output into multiple files.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "stackdistance.useMultipleOutputFiles", false);

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		AnalysisInstrumentationStep analysisInstrumentationStep = createAndInitializeExecutionStep(
				AnalysisInstrumentationStep.class);
		AnalysisInformationCollectorStep analysisInformationCollectorStep = createAndInitializeExecutionStep(
				AnalysisInformationCollectorStep.class);
		CleanupStep cleanupStep = createAndInitializeExecutionStep(CleanupStep.class);

		analysisInformationCollectorStep.setUseMultiFileOutput(
				configuration.getDynamicValues().getBooleanValue(CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES));

		prepareStep.start();
		analysisInstrumentationStep.start();
		analysisInformationCollectorStep.start();
		cleanupStep.start();
	}
}
