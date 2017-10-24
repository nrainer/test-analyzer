package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager;

import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

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
		m_threadAwareStackDistanceManager.beforeAllTests();
	}

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		m_threadAwareStackDistanceManager.startStackLogger(testCaseIdentifier);
	}
}
