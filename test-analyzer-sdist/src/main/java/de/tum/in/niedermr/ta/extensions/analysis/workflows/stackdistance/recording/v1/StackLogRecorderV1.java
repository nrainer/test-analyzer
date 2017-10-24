package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v1;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.StackLogDataManager;

/**
 * Stack log recorder V1.<br/>
 * Used by instrumented code. DO NOT MODIFY.
 * 
 * @see StackLogDataManager to retrieve the logged data
 */
public class StackLogRecorderV1 {
	private static int s_currentStackDistance;

	/**
	 * Start a new log for the specified test case. This resets all counters.<br/>
	 * Note that it is ok to log invocations from framing methods (<code>@Before</code>) too because they also invoke
	 * the mutated methods.
	 */
	public static synchronized void startLog(TestcaseIdentifier testCaseIdentifier) {
		s_currentStackDistance = 0;
		StackLogDataManager.resetAndStart(testCaseIdentifier);
	}

	/**
	 * Push invocation: The invocation is started, the test case just invoked this method (directly or indirectly).<br/>
	 * (This method is invoked by instrumented code.)
	 */
	public static synchronized void pushInvocation(String methodIdentifierString) {
		s_currentStackDistance++;

		MethodIdentifier methodIdentifier = MethodIdentifier.parse(methodIdentifierString);
		StackLogDataManager.visitMethodInvocation(methodIdentifier, s_currentStackDistance);
	}

	/**
	 * Pop invocation: The invocation is completed, the test case is about to leave the method.
	 */
	public static synchronized void popInvocation() {
		if (s_currentStackDistance == 0) {
			return;
		}

		s_currentStackDistance--;
	}
}
