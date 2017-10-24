package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager;

import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.AbstractThreadAwareStackDistanceManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v2.ThreadStackManager;

/**
 * Logic to collect information about the test cases and methods under
 * test.<br/>
 */
public class AbstractThreadAwareStackInformationCollectionLogic extends AbstractStackInformationCollectionLogic {

	private AbstractThreadAwareStackDistanceManager m_threadAwareStackDistanceManager;

	public AbstractThreadAwareStackInformationCollectionLogic(
			AbstractThreadAwareStackDistanceManager threadAwareStackDistanceManager) {
		m_threadAwareStackDistanceManager = threadAwareStackDistanceManager;
	}

	/** {@inheritDoc} */
	@Override
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		super.execBeforeExecutingAllTests(testClassesWithTestcases);
		ThreadStackManager stackManager = new ThreadStackManager();
		m_threadAwareStackDistanceManager.execSetThreadStackManagerAndVerify(stackManager);
	}

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		m_threadAwareStackDistanceManager.execStartStackLogger(testCaseIdentifier);
	}
}
