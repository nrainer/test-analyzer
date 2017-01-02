package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance2;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogDataManager;

/**
 * Stack log recorder V2.<br/>
 * Used by instrumented code. DO NOT MODIFY.
 * 
 * @see StackLogDataManager to retrieve the logged data
 */
public class StackLogRecorderV2 {

	/** Manages the stack heights of started threads. */
	private static ThreadStackManager s_threadStackManager;

	/** Set the thread manager and verify that the modified thread class was loaded. */
	public static void setThreadStackManagerAndVerify(ThreadStackManager threadManager) {
		s_threadStackManager = threadManager;
		s_threadStackManager.verifyReplacedThreadClassInUse();
	}

	/**
	 * Start a new log for the specified test case. This resets all counters.<br/>
	 * Note that it is ok to log invocations from framing methods (<code>@Before</code>) too because they also invoke
	 * the mutated methods.
	 */
	public static synchronized void startLog(TestcaseIdentifier testCaseIdentifier) {
		StackLogDataManager.resetAndStart(testCaseIdentifier);
		s_threadStackManager.setStopClassName(testCaseIdentifier.toMethodIdentifier().getOnlyClassName());
	}

	/**
	 * Push invocation: The invocation is started, the test case just invoked this method (directly or indirectly).<br/>
	 * (This method is invoked by instrumented code.)
	 */
	public static synchronized void pushInvocation(String methodIdentifierString) {
		MethodIdentifier methodIdentifier = MethodIdentifier.parse(methodIdentifierString);
		int currentStackDistance = s_threadStackManager.computeCurrentStackHeight(StackLogRecorderV2.class.getName());

		StackLogDataManager.visitMethodInvocation(methodIdentifier, currentStackDistance);
	}

	/**
	 * Pop invocation: The invocation is completed, the test case is about to leave the method.
	 */
	public static synchronized void popInvocation() {
		// NOP: not needed (but keep the method because the instrumented code invokes it)
	}
}
