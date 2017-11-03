package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

/**
 * Stack log recorder for tests.
 */
public class StackLogRecorderForTestingPurposes {

	public static TestcaseIdentifier s_testcaseIdentifier;
	public static List<String> s_methodIdentifierStrings;
	public static int s_pushInvocationCount = 0;
	public static int s_popInvocationCount = 0;

	public static synchronized void startLog(TestcaseIdentifier testCaseIdentifier) {
		s_testcaseIdentifier = testCaseIdentifier;
	}

	public static synchronized void pushInvocation(String methodIdentifierString) {
		s_pushInvocationCount++;
		s_methodIdentifierStrings.add(methodIdentifierString);
	}

	public static synchronized void popInvocation() {
		s_popInvocationCount++;
	}

	public static void reset() {
		s_testcaseIdentifier = null;
		s_methodIdentifierStrings = new ArrayList<>();
		s_pushInvocationCount = 0;
		s_popInvocationCount = 0;
	}
}
