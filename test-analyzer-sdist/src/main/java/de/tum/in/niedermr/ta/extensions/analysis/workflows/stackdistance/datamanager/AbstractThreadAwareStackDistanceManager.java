package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.threading.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.threads.ThreadNotifier;

public abstract class AbstractThreadAwareStackDistanceManager {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractThreadAwareStackDistanceManager.class);

	/**
	 * Prefixes of class names that should not be counted when computing the stack distance.
	 */
	private static final String[] STACK_COUNT_IGNORE_CLASS_NAME_PREFIXES = new String[] { "org.junit.", "sun.reflect.",
			"junit.framework.", "org.testng.", "org.apache.maven.surefire.", "org.apache.maven.failsafe." };

	public void beforeAllTests() {
		LOGGER.info("Used stack distance manager is: " + getClass().getName());
		ThreadStackManager stackManager = new ThreadStackManager();

		// useful for JUnit tests with timeout annotation
		stackManager.setStackCountIgnoreClassNamesPrefixes(STACK_COUNT_IGNORE_CLASS_NAME_PREFIXES);

		ThreadNotifier.INSTANCE.registerListener(stackManager);
		LOGGER.info("Registered " + ThreadStackManager.class.getName() + " at " + ThreadNotifier.class.getName() + ".");
		execSetThreadStackManagerAndVerify(stackManager);
		LOGGER.info("ThreadStackManager is set and verified.");
	}

	/** Set the {@link ThreadStackManager} and verify it. */
	protected abstract void execSetThreadStackManagerAndVerify(ThreadStackManager stackManager);

	public abstract void startStackLogger(TestcaseIdentifier testcaseIdentifier);
}
