package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v1;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.AbstractStackInformationCollectionLogic;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v1.StackLogRecorderV1;

/**
 * Logic to collect information about the test cases and methods under test. <br/>
 * Parameterless constructor required.
 */
public class StackInformationCollectionLogicV1 extends AbstractStackInformationCollectionLogic {

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		StackLogRecorderV1.startLog(testCaseIdentifier);
	}
}
