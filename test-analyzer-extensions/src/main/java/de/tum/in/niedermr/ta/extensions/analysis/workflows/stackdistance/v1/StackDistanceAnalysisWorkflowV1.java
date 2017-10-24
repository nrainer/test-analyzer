package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v1;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AbstractStackDistanceAnalysisWorkflow;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v1.StackInformationCollectionLogicV1;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v1.StackLogRecorderV1;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInformationCollectorStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInstrumentationStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v3.StackDistanceAnalysisWorkflowV3;

/**
 * Computes the minimum and maximum distance on the call stack between test case
 * and method. <br/>
 * <b>Deprecated: {@link StackDistanceAnalysisWorkflowV3} should be used
 * instead.</b> V2 produces valid results for multi-threaded code. Note that V2
 * requires to replace the {@link Thread} class.
 */
public class StackDistanceAnalysisWorkflowV1 extends AbstractStackDistanceAnalysisWorkflow {

	/** {@inheritDoc} */
	@Override
	protected AnalysisInstrumentationStep createAnalysisInstrumentationStep() {
		AnalysisInstrumentationStep step = createAndInitializeExecutionStep(AnalysisInstrumentationStep.class);
		step.setStackLogRecorderClass(StackLogRecorderV1.class);
		return step;
	}

	/** {@inheritDoc} */
	@Override
	protected AnalysisInformationCollectorStep createAnalysisInformationCollectorStep() {
		AnalysisInformationCollectorStep step = createAndInitializeExecutionStep(
				AnalysisInformationCollectorStep.class);
		step.setResultOutputFile(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V1);
		step.setInformationCollectorLogicClass(StackInformationCollectionLogicV1.class);
		return step;
	}
}
