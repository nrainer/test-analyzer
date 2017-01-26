package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v1.logic.collection;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection.AbstractAnalysisInformationCollectionLogic;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v1.logic.recording.StackLogRecorderV1;

/**
 * Logic to collect information about the test cases and methods under test. <br/>
 * Parameterless constructor required.
 */
public class AnalysisInformationCollectionLogicV1 extends AbstractAnalysisInformationCollectionLogic {

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		StackLogRecorderV1.startLog(testCaseIdentifier);
	}
}
