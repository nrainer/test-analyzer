package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import java.util.HashMap;
import java.util.Map;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

/**
 * Stack logger.<br/>
 * Used by instrumented code. DO NOT MODIFY.
 */
public class StackLogger {
	private static TestcaseIdentifier s_currentTestCaseIdentifier;
	private static int s_currentStackDistance;
	private static Map<MethodIdentifier, Integer> s_invocationsMinDistance = new HashMap<>();
	private static Map<MethodIdentifier, Integer> s_invocationsMaxDistance = new HashMap<>();
	private static Map<MethodIdentifier, Integer> s_invocationsCount = new HashMap<>();

	/**
	 * Start a new log for the specified test case. This resets all
	 * counters.<br/>
	 * Note that it is ok to log invocations from framing methods
	 * (<code>@Before</code>) too because they also invoke the mutated methods.
	 */
	public static synchronized void startLog(TestcaseIdentifier testCaseIdentifier) {
		s_currentTestCaseIdentifier = testCaseIdentifier;
		resetLog();
	}

	/** Reset the counters. */
	private static synchronized void resetLog() {
		s_currentStackDistance = 0;
		s_invocationsMinDistance.clear();
		s_invocationsMaxDistance.clear();
		s_invocationsCount.clear();
	}

	/**
	 * Push invocation: The invocation is started, the test case just invoked
	 * this method (directly or indirectly).<br/>
	 * (This method is invoked by instrumented code.)
	 */
	public static synchronized void pushInvocation(String methodIdentifierString) {
		s_currentStackDistance++;

		MethodIdentifier methodIdentifier = MethodIdentifier.parse(methodIdentifierString);

		Integer minInvocation = s_invocationsMinDistance.get(methodIdentifier);
		if (minInvocation == null || s_currentStackDistance < minInvocation) {
			s_invocationsMinDistance.put(methodIdentifier, s_currentStackDistance);
		}

		Integer maxInvocation = s_invocationsMaxDistance.get(methodIdentifier);
		if (maxInvocation == null || s_currentStackDistance > maxInvocation) {
			s_invocationsMaxDistance.put(methodIdentifier, s_currentStackDistance);
		}

		s_invocationsCount.putIfAbsent(methodIdentifier, 0);
		s_invocationsCount.put(methodIdentifier, 1 + s_invocationsCount.get(methodIdentifier));
	}

	/**
	 * Pop invocation: The invocation is completed, the test case is about to
	 * leave the method.
	 */
	public static synchronized void popInvocation() {
		if (s_currentStackDistance == 0) {
			return;
		}

		s_currentStackDistance--;
	}

	/** Get the identifier of the current test case. */
	public static TestcaseIdentifier getCurrentTestCaseIdentifier() {
		return s_currentTestCaseIdentifier;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by the
	 * current testcase the minimum stack distance between the testcase and the
	 * method.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsMinDistance() {
		return s_invocationsMinDistance;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by the
	 * current testcase the maximum stack distance between the testcase and the
	 * method.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsMaxDistance() {
		return s_invocationsMaxDistance;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by the
	 * current testcase the number of invocations.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsCount() {
		return s_invocationsCount;
	}
}
