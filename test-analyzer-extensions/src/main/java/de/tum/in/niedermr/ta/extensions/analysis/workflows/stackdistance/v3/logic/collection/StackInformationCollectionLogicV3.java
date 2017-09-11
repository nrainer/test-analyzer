package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v3.logic.collection;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection.AbstractThreadAwareStackInformationCollectionLogic;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.collection.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v3.recording.StackLogRecorderV3;

/**
 * Logic to collect information about the test cases and methods under test.<br/>
 * Parameterless constructor required.
 */
public class StackInformationCollectionLogicV3 extends AbstractThreadAwareStackInformationCollectionLogic {

	/** {@inheritDoc} */
	@Override
	protected void execSetThreadStackManagerAndVerify(ThreadStackManager stackManager) {
		StackLogRecorderV3.setThreadStackManagerAndVerify(stackManager);
	}

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		StackLogRecorderV3.startLog(testCaseIdentifier);
	}
}
