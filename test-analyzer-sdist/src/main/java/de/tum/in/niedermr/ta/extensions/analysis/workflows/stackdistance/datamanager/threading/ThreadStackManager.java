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

	/** Whether the {@link Thread} class should be verified. Can be disabled during testing this class. */
	private static boolean s_threadClassVerificationEnabled = true;

	/** Map that contains for a thread its creator thread (key: thread's name, value: creator thread's name). */
	private final Map<String, String> m_parentThreadNameByThreadName = new HashMap<>();
	/**
	 * Map that contains for a thread its stack height from the program start (key: thread's name, value: stack height
	 * at thread start).
	 */
	private final Map<String, Integer> m_stackHeightAtCreationByThreadName = new HashMap<>();

	/**
	 * Stop class from where to stop counting the stacks. Occurrences of this class will be excluded.
	 */
	private String m_stopClassName;

	/** Prefixes for qualified class names that should be ignored when counting the stack distance. */
	private String[] m_stackCountIgnoreClassNamePrefixes;

	/** {@inheritDoc} */
	@Override
	public synchronized void threadIsAboutToStart(String nameOfThreadToBeStarted) {
		// new thread will be started by the current thread
		String nameOfCreatorThread = Thread.currentThread().getName();
		LOGGER.debug("Thread " + nameOfThreadToBeStarted + " is being started by thread " + nameOfCreatorThread);

		m_parentThreadNameByThreadName.put(nameOfThreadToBeStarted, nameOfCreatorThread);
		int stackHeightAtCreation = computeFullStackHeightOfCurrentThread(nameOfThreadToBeStarted);

		if (stackHeightAtCreation < 0) {
			throw new IllegalStateException("Computed negative stack height for creator thread " + nameOfCreatorThread);
		}

		m_stackHeightAtCreationByThreadName.put(nameOfThreadToBeStarted, stackHeightAtCreation);
		LOGGER.debug("Registered thread " + nameOfThreadToBeStarted + " with stack height " + stackHeightAtCreation);
	}

	/**
	 * Verify that the modified {@link Thread} is used.
	 * 
	 * @throws IllegalStateException
	 *             if the original {@link Thread} class is in use
	 */
	public static void verifyReplacedThreadClassInUse() {
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
	public synchronized int computeCurrentStackHeight(Class<?> startClass) {
		String currentThreadName = Thread.currentThread().getName();

		int stackHeight = getStackHeightOfThreadAtCreation(currentThreadName)
				+ computeStackHeightOnCurrentThreadOnly(startClass);

		if (stackHeight < 0) {
			LOGGER.error("Negative stack height computed in thread " + currentThreadName);
			stackHeight = 0;
		}

		return stackHeight;
	}

	/**
	 * Compute the full stack height of the thread, including the height for creating this thread.
	 */
	private synchronized int computeFullStackHeightOfCurrentThread(String creatorThreadName) {
		// stack height of the creator thread, without creator's creator thread height
		int stackHeightOnCurrentThread = computeStackHeightOnCurrentThreadOnly(ThreadNotifier.class);

		if (m_parentThreadNameByThreadName.containsKey(creatorThreadName)) {
			// creator thread has a creator
			return stackHeightOnCurrentThread + getStackHeightOfThreadAtCreation(creatorThreadName);
		}

		// current thread is the initial thread and has no creator
		return stackHeightOnCurrentThread;
	}

	/**
	 * Get the stored stack height of the given thread at its creation. Use 0 if no information is available.
	 * 
	 * @param threadName
	 *            name of the thread
	 */
	public synchronized int getStackHeightOfThreadAtCreation(String threadName) {
		Integer threadStackHeightAtStart = m_stackHeightAtCreationByThreadName.get(threadName);

		if (threadStackHeightAtStart != null) {
			return threadStackHeightAtStart;
		}

		m_stackHeightAtCreationByThreadName.put(threadName, 0);

		if (!MAIN_THREAD_NAME.equals(threadName)) {
			LOGGER.warn("No start stack height available for thread " + threadName + ". Using 0.");
		}

		return 0;
	}

	/**
	 * Compute the current height on the stack, <b>without</b> the height for creating this thread. Considered as an
	 * expensive operation, because an exception is created to retrieve the stack trace.
	 * 
	 * @param startClassName
	 *            start counting after this class (this class excluded)
	 */
	protected synchronized int computeStackHeightOnCurrentThreadOnly(Class<?> startClass) {
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		return computeStackHeightOfStackTrace(startClass.getName(), m_stopClassName, stackTrace,
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

			if (shouldClassBeIgnoredWhenCounting(stackElementClassName, ignoredClassNamePrefixes)) {
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
	private static boolean shouldClassBeIgnoredWhenCounting(String stackElementClassName,
			String[] ignoredClassNamePrefixes) {
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
