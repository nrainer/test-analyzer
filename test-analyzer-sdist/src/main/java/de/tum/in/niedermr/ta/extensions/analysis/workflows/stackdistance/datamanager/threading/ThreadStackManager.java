package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.threading;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.extensions.threads.IModifiedThreadClass;
import de.tum.in.niedermr.ta.extensions.threads.IThreadListener;
import de.tum.in.niedermr.ta.extensions.threads.ThreadNotifier;

/** Records the stack height of threads. */
public class ThreadStackManager implements IThreadListener {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ThreadStackManager.class);

	/** Name of the main thread. */
	private static final String MAIN_THREAD_NAME = "main";

	private static boolean s_threadClassVerificationEnabled = true;

	/** Map that contains for a thread its creator thread. */
	private final Map<String, String> m_threadNameStartedByThreadName = new HashMap<>();
	/**
	 * Map that contains for a thread its stack height from the program start.
	 */
	private final Map<String, Integer> m_stackHeightAtStartByThreadName = new HashMap<>();

	/**
	 * Stop class from where to stop counting the stacks. Occurrences of this class will be excluded.
	 */
	private String m_stopClassName;

	/**
	 * Class names which start with one of the specified prefixes will not be counted when computing the stack distance.
	 */
	private String[] m_stackCountIgnoreClassNamePrefixes;

	/** {@inheritDoc} */
	@Override
	public synchronized void threadIsAboutToStart(String startingThreadName) {
		String creatorThreadName = Thread.currentThread().getName();
		LOGGER.debug("Thread " + startingThreadName + " is being started by thread " + creatorThreadName);

		m_threadNameStartedByThreadName.put(startingThreadName, creatorThreadName);
		int stackHeightAtCreation = computeStackHeightOfThread(startingThreadName);

		if (stackHeightAtCreation < 0) {
			throw new IllegalStateException("Computed negative stack height for thread " + startingThreadName);
		}

		m_stackHeightAtStartByThreadName.put(startingThreadName, stackHeightAtCreation);
		LOGGER.debug("Registered thread " + startingThreadName + " with stack height " + stackHeightAtCreation);
	}

	/**
	 * Verify that the modified {@link Thread} is used.
	 * 
	 * @throws IllegalStateException
	 *             if the original {@link Thread} class is in use
	 */
	public void verifyReplacedThreadClassInUse() {
		if (!s_threadClassVerificationEnabled) {
			LOGGER.warn("Thread class verification is disabled!");
			return;
		}

		if (IModifiedThreadClass.class.isAssignableFrom(Thread.class)) {
			LOGGER.info("OK: Modified Thread class is in use.");
			return;
		}

		LOGGER.error("It appears that the original " + Thread.class.getName()
				+ " class is used instead of the modified one, which implements " + IModifiedThreadClass.class.getName()
				+ "! Either put the modified Thread class into the endorsed folder or use a non-thread aware version.");
		LOGGER.error("Classpath is: " + ClasspathUtility.getCurrentClasspath());

		throw new IllegalStateException("It appears that the original " + Thread.class.getName()
				+ " class is used instead of the modified one!");
	}

	/** {@link #m_stopClassName} */
	public synchronized void setStopClassName(String stopClassName) {
		m_stopClassName = stopClassName;
	}

	/** {@link #m_stackCountIgnoreClassNamePrefixes} */
	public synchronized void setStackCountIgnoreClassNamesPrefixes(String[] stackCountIgnoreClassNamePrefixes) {
		m_stackCountIgnoreClassNamePrefixes = stackCountIgnoreClassNamePrefixes;
	}

	/**
	 * Compute the current stack height, including the height for creating this thread.
	 * 
	 * @param startClassName
	 *            start counting the stack elements (top down) after this class
	 */
	public synchronized int computeCurrentStackHeight(String startClassName) {
		String currentThreadName = Thread.currentThread().getName();

		int stackHeight = getStartStackHeightOfThread(currentThreadName)
				+ computeStackHeightOnCurrentThreadOnly(startClassName);

		if (stackHeight < 0) {
			LOGGER.error("Negative stack height computed in thread " + currentThreadName);
			stackHeight = 0;
		}

		return stackHeight;
	}

	/**
	 * Compute the stack height of the thread, including the height for creating this thread.
	 */
	private synchronized int computeStackHeightOfThread(String threadCreatorName) {
		// stack height of the creator thread, without creator's creator thread height
		int creatorThreadCurrentOwnStackHeight = computeStackHeightOnCurrentThreadOnly(ThreadNotifier.class.getName());

		if (m_threadNameStartedByThreadName.containsKey(threadCreatorName)) {
			// creator thread has a creator
			return creatorThreadCurrentOwnStackHeight + getStartStackHeightOfThread(threadCreatorName);
		}

		// current thread is the initial thread and has no creator
		return creatorThreadCurrentOwnStackHeight;
	}

	/** Get the stored stack height of the given thread. Return 0 if no information is available. */
	public synchronized int getStartStackHeightOfThread(String threadName) {
		Integer threadCreatorStackHeight = m_stackHeightAtStartByThreadName.get(threadName);

		if (threadCreatorStackHeight != null) {
			return threadCreatorStackHeight;
		}

		m_stackHeightAtStartByThreadName.put(threadName, 0);

		if (!MAIN_THREAD_NAME.equals(threadName)) {
			LOGGER.warn("No start stack height available for thread " + threadName + ". Using 0.");
		}

		return 0;
	}

	/**
	 * Compute the current height on the stack, <b>without</b> the height for creating this thread.
	 * 
	 * @param startClassName
	 *            start counting after this class (this class excluded)
	 */
	protected synchronized int computeStackHeightOnCurrentThreadOnly(String startClassName) {
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		return computeStackHeightOfStackTrace(startClassName, m_stopClassName, stackTrace,
				m_stackCountIgnoreClassNamePrefixes);
	}

	/**
	 * Compute the height of the stack trace.
	 * 
	 * @param startClassName
	 *            start counting after this class (this class excluded)
	 * @param stopClassName
	 *            stop counting when this class is reached (this class is not counted)
	 */
	protected static int computeStackHeightOfStackTrace(String startClassName, String stopClassName,
			StackTraceElement[] stackTrace, String[] ignoredClassNamePrefixes) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start class name is: " + startClassName);
			LOGGER.debug("Stop class name is: " + stopClassName);
		}

		int count = 0;
		boolean startClassReached = false;
		boolean startClassCompleted = false;

		for (StackTraceElement stackTraceElement : stackTrace) {
			String stackElementClassName = stackTraceElement.getClassName();
			String stackElementString = stackTraceElement.toString();

			if (!startClassCompleted) {
				boolean inStartClass = startClassName.equals(stackElementClassName);

				if (!startClassReached && inStartClass) {
					// start class reached -> start counting when the start
					// class is left
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Start class reached: " + stackElementString);
					}
					startClassReached = true;
				} else if (startClassReached && inStartClass) {
					// start class was reached and we are still in it
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Ignoring element in start class: " + stackElementString);
					}
				} else if (startClassReached && !inStartClass) {
					// start class was reached and no longer in start class ->
					// first element to count reached
					startClassCompleted = true;
				}
			}

			if (!startClassCompleted) {
				continue;
			}

			if (stopClassName.equals(stackElementClassName)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Abort counting at element: " + stackElementString);
				}
				break;
			}

			if (isCountIgnoredClass(stackElementClassName, ignoredClassNamePrefixes)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Skipping ignored element: " + stackElementString);
				}
				continue;
			}

			count++;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Counted element: " + stackElementString);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Stack height is: " + count);
		}

		return count;
	}

	/** Check if the class should not be counted. */
	private static boolean isCountIgnoredClass(String stackElementClassName, String[] ignoredClassNamePrefixes) {
		for (String classNamePrefix : ignoredClassNamePrefixes) {
			if (stackElementClassName.startsWith(classNamePrefix)) {
				return true;
			}
		}

		return false;
	}

	/** Re-enable the thread class verification. */
	public static void enableThreadClassVerification() {
		s_threadClassVerificationEnabled = true;
	}

	/** Disable the thread class verification. Only intended for use in certain test cases. */
	public static void disableThreadClassVerification() {
		s_threadClassVerificationEnabled = false;

	}
}
