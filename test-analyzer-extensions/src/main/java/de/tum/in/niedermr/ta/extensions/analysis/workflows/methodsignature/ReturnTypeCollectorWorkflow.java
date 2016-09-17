package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps.ReturnTypeCollectorStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.SimplePersistResultStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Workflow that collects the declared return types of methods under test. */
public class ReturnTypeCollectorWorkflow extends AbstractWorkflow {

	/** Result file name. */
	private static final String RESULT_FILE_NAME = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "return-type-list"
			+ FileSystemConstants.FILE_EXTENSION_TXT;

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();

		SimplePersistResultStep resultPersistStep = createAndInitializeExecutionStep(SimplePersistResultStep.class);

		ReturnTypeCollectorStep collectorStep = createAndInitializeExecutionStep(ReturnTypeCollectorStep.class);
		collectorStep.setResultReceiver(resultPersistStep);
		collectorStep.start();

		resultPersistStep.setResultFileName(RESULT_FILE_NAME);
		resultPersistStep.start();
	}
}
