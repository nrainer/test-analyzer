package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.collection.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.threads.ThreadNotifier;

/**
 * Logic to collect information about the test cases and methods under test.<br/>
 * Parameterless constructor required.
 */
public abstract class AbstractThreadAwareStackInformationCollectionLogic
		extends AbstractStackInformationCollectionLogic {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractThreadAwareStackInformationCollectionLogic.class);

	/** Prefixes of class names that should not be counted when computing the stack distance. */
	private static final String[] STACK_COUNT_IGNORE_CLASS_NAME_PREFIXES = new String[] { "org.junit.",
			"sun.reflect." };

	/** {@inheritDoc} */
	@Override
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		super.execBeforeExecutingAllTests(testClassesWithTestcases);
		ThreadStackManager stackManager = new ThreadStackManager();

		// useful for JUnit tests with timeout annotation
		stackManager.setStackCountIgnoreClassNamesPrefixes(STACK_COUNT_IGNORE_CLASS_NAME_PREFIXES);

		ThreadNotifier.INSTANCE.registerListener(stackManager);
		LOGGER.info("Registered StackManager at the ThreadNotifier.");
		execSetThreadStackManagerAndVerify(stackManager);
		LOGGER.info("ThreadStackManager is set and verified.");
	}

	/** Set the {@link ThreadStackManager} and verify it. */
	protected abstract void execSetThreadStackManagerAndVerify(ThreadStackManager stackManager);
}
