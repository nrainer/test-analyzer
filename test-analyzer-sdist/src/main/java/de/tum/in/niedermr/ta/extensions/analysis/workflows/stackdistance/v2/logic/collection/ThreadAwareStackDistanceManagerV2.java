package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.collection;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection.AbstractThreadAwareStackDistanceManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.recording.StackLogRecorderV2;

public class ThreadAwareStackDistanceManagerV2 extends AbstractThreadAwareStackDistanceManager {

	/** {@inheritDoc} */
	@Override
	protected void execSetThreadStackManagerAndVerify(ThreadStackManager stackManager) {
		StackLogRecorderV2.setThreadStackManagerAndVerify(stackManager);
	}

	/** {@inheritDoc} */
	@Override
	protected void execStartStackLogger(TestcaseIdentifier testcaseIdentifier) {
		StackLogRecorderV2.startLog(testcaseIdentifier);
	}

}
