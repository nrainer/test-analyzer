package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance2.collection;

import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.collection.AbstractAnalysisInformationCollectionLogic;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance2.StackLogRecorderV2;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance2.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.threads.ThreadNotifier;

/**
 * Logic to collect information about the test cases and methods under test.<br/>
 * Parameterless constructor required.
 */
public class AnalysisInformationCollectionLogicV2 extends AbstractAnalysisInformationCollectionLogic {

	@Override
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		super.execBeforeExecutingAllTests(testClassesWithTestcases);
		ThreadStackManager stackManager = new ThreadStackManager();
		ThreadNotifier.INSTANCE.registerListener(stackManager);
		StackLogRecorderV2.setThreadStackManagerAndVerify(stackManager);
	}

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		StackLogRecorderV2.startLog(testCaseIdentifier);
	}
}
