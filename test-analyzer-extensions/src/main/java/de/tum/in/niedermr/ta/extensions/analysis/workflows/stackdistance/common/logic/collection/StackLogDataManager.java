package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection;

import java.util.HashMap;
import java.util.Map;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

/** Manages the data recorded by a stack log recorder. */
public class StackLogDataManager {
	private static TestcaseIdentifier s_currentTestCaseIdentifier;
	private static Map<MethodIdentifier, Integer> s_invocationsMinDistance = new HashMap<>();
	private static Map<MethodIdentifier, Integer> s_invocationsMaxDistance = new HashMap<>();
	private static Map<MethodIdentifier, Integer> s_invocationsCount = new HashMap<>();

	/** Start a new log for the specified test case and reset all counters. */
	public static synchronized void resetAndStart(TestcaseIdentifier testCaseIdentifier) {
		resetLog();
		s_currentTestCaseIdentifier = testCaseIdentifier;
	}

	/** Reset the counters. */
	private static synchronized void resetLog() {
		s_currentTestCaseIdentifier = null;
		s_invocationsMinDistance.clear();
		s_invocationsMaxDistance.clear();
		s_invocationsCount.clear();
	}

	/** Record a visited method: Update the minimal and maximal stack distance and the invocation count. */
	public static synchronized void visitMethodInvocation(MethodIdentifier methodIdentifier, int stackDistance) {
		// count the invocations
		s_invocationsCount.put(methodIdentifier, 1 + s_invocationsCount.getOrDefault(methodIdentifier, 0));

		// update the minimal stack distance
		Integer minStackDistance = s_invocationsMinDistance.get(methodIdentifier);
		if (minStackDistance == null || stackDistance < minStackDistance) {
			s_invocationsMinDistance.put(methodIdentifier, stackDistance);
		}

		// update the maximal stack distance
		Integer maxStackDistance = s_invocationsMaxDistance.get(methodIdentifier);
		if (maxStackDistance == null || stackDistance > maxStackDistance) {
			s_invocationsMaxDistance.put(methodIdentifier, stackDistance);
		}
	}

	/** Get the identifier of the current test case. */
	public static TestcaseIdentifier getCurrentTestCaseIdentifier() {
		return s_currentTestCaseIdentifier;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by the current testcase the minimum stack distance
	 * between the testcase and the method.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsMinDistance() {
		return s_invocationsMinDistance;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by the current testcase the maximum stack distance
	 * between the testcase and the method.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsMaxDistance() {
		return s_invocationsMaxDistance;
	}

	/**
	 * Get for each method which is (directly or indirectly) invoked by the current testcase the number of invocations.
	 */
	public static Map<MethodIdentifier, Integer> getInvocationsCount() {
		return s_invocationsCount;
	}
}
