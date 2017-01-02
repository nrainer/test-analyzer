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

	/** Prefixes of class names that should not be counted when computing the stack distance. */
	private static final String[] STACK_COUNT_IGNORE_CLASS_NAME_PREFIXES = new String[] { "org.junit.",
			"sun.reflect." };

	@Override
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		super.execBeforeExecutingAllTests(testClassesWithTestcases);
		ThreadStackManager stackManager = new ThreadStackManager();

		// useful for JUnit tests with specified timeouts
		stackManager.setStackCountIgnoreClassNamesPrefixes(STACK_COUNT_IGNORE_CLASS_NAME_PREFIXES);

		ThreadNotifier.INSTANCE.registerListener(stackManager);
		StackLogRecorderV2.setThreadStackManagerAndVerify(stackManager);
	}

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		StackLogRecorderV2.startLog(testCaseIdentifier);
	}
}
