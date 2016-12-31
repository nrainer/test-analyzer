package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInformationCollectorStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInstrumentationStep;

/**
 * Computes the minimum and maximum distance on the call stack between test case and method.
 */
public class StackDistanceAnalysisWorkflow extends AbstractStackDistanceAnalysisWorkflow {

	/** {@inheritDoc} */
	@Override
	protected AnalysisInstrumentationStep createAnalysisInstrumentationStep() {
		return createAndInitializeExecutionStep(AnalysisInstrumentationStep.class);
	}

	/** {@inheritDoc} */
	@Override
	protected AnalysisInformationCollectorStep createAnalysisInformationCollectorStep() {
		return createAndInitializeExecutionStep(AnalysisInformationCollectorStep.class);
	}
}
