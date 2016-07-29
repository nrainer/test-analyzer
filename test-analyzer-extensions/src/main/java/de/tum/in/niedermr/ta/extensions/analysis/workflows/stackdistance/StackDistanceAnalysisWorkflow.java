package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s1.AnalysisInstrumentationStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s2.AnalysisInformationCollectorStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s3.TearDownStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.preparation.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * Computes the minimum and maximum distance on the call stack between test case and method.
 */
public class StackDistanceAnalysisWorkflow extends AbstractWorkflow {

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		AnalysisInstrumentationStep analysisInstrumentationStep = createAndInitializeExecutionStep(
				AnalysisInstrumentationStep.class);
		AnalysisInformationCollectorStep analysisInformationCollectorStep = createAndInitializeExecutionStep(
				AnalysisInformationCollectorStep.class);
		TearDownStep cleanupStep = createAndInitializeExecutionStep(TearDownStep.class);

		prepareStep.run();
		analysisInstrumentationStep.run();
		analysisInformationCollectorStep.run();
		cleanupStep.run();
	}
}
