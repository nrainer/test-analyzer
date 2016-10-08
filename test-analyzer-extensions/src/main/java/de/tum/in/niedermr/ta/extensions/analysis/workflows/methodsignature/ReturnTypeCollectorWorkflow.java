package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps.ReturnTypeCollectorStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Workflow that collects the declared return types of methods under test. */
public class ReturnTypeCollectorWorkflow extends AbstractWorkflow {

	/**
	 * <code>extension.methodsignature.useMultipleOutputFiles</code>: Split the
	 * output into multiple files.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "methodsignature.useMultipleOutputFiles", false);

	/** Result file name. */
	private static final String RESULT_FILE_NAME = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "return-type-list"
			+ FileSystemConstants.FILE_EXTENSION_TXT;

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();

		boolean useMultipleOutputFiles = configuration.getDynamicValues()
				.getBooleanValue(CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES);
		String resultFileName = getFileInWorkingArea(context, RESULT_FILE_NAME);
		IResultReceiver resultReceiver = ResultReceiverFactory
				.createFileResultReceiverWithDefaultSettings(useMultipleOutputFiles, resultFileName);

		ReturnTypeCollectorStep collectorStep = createAndInitializeExecutionStep(ReturnTypeCollectorStep.class);
		collectorStep.setResultReceiver(resultReceiver);
		collectorStep.start();
	}
}
