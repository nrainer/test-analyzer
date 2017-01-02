package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisInformationCollectorV1;

/** Analysis information collector step. */
public class AnalysisInformationCollectorStepV1 extends AbstractAnalysisInformationCollectorStep {

	/** {@inheritDoc} */
	@Override
	protected Class<?> getInformationCollectorClass() {
		return AnalysisInformationCollectorV1.class;
	}
}
