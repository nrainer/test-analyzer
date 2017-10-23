package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v3.logic.collection;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection.AbstractThreadAwareStackDistanceManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.collection.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v3.recording.StackLogRecorderV3;

public class ThreadAwareStackDistanceManagerV3 extends AbstractThreadAwareStackDistanceManager {

	/** {@inheritDoc} */
	@Override
	protected void execSetThreadStackManagerAndVerify(ThreadStackManager stackManager) {
		StackLogRecorderV3.setThreadStackManagerAndVerify(stackManager);
	}

	/** {@inheritDoc} */
	@Override
	protected void execStartStackLogger(TestcaseIdentifier testcaseIdentifier) {
		StackLogRecorderV3.startLog(testcaseIdentifier);
	}

}
