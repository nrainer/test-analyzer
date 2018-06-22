package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v2;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.AbstractThreadAwareStackDistanceManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.threading.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v2.StackLogRecorderV2;

public class ThreadAwareStackDistanceManagerV2 extends AbstractThreadAwareStackDistanceManager {

	/** {@inheritDoc} */
	@Override
	protected void execSetThreadStackManagerAndVerify(ThreadStackManager stackManager) {
		StackLogRecorderV2.setThreadStackManagerAndVerify(stackManager);
	}

	/** {@inheritDoc} */
	@Override
	public void startStackLogger(TestcaseIdentifier testcaseIdentifier) {
		StackLogRecorderV2.startLog(testcaseIdentifier);
	}
}
