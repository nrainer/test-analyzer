package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import java.util.HashMap;
import java.util.Map;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

public class StackLogger {
	private static TestcaseIdentifier s_currentTestCaseIdentifier;
	private static int s_currentStackDistance;
	private static Map<MethodIdentifier, Integer> s_invocationsMinDistance = new HashMap<>();
	private static Map<MethodIdentifier, Integer> s_invocationsMaxDistance = new HashMap<>();

	/**
	 * It is ok to log invocations from framing methods (@Before) too because they also invoke the mutated methods.
	 */
	public static synchronized void startLog(TestcaseIdentifier testCaseIdentifier) {
		s_currentTestCaseIdentifier = testCaseIdentifier;
		resetLog();
	}

	private static synchronized void resetLog() {
		s_currentStackDistance = 0;
		s_invocationsMinDistance.clear();
		s_invocationsMaxDistance.clear();
	}

	/**
	 * Invoked by instrumented code. <b>DO NOT MODIFY.</b>
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
	}

	public static synchronized void popInvocation() {
		if (s_currentStackDistance == 0) {
			return;
		}

		s_currentStackDistance--;
	}

	public static TestcaseIdentifier getCurrentTestCaseIdentifier() {
		return s_currentTestCaseIdentifier;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by a testcase the minimum stack distance between the testcase and the method.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsMinDistance() {
		return s_invocationsMinDistance;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by a testcase the maximum stack distance between the testcase and the method.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsMaxDistance() {
		return s_invocationsMaxDistance;
	}
}
