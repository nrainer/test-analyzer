package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

public class StackLoggerTests {
	@Test
	public void testStartLog() {
		TestcaseIdentifier testcaseIdentifier1 = TestcaseIdentifier
				.parse("CommonTest" + TestcaseIdentifier.SEPARATOR + "testcase1");
		StackLogger.startLog(testcaseIdentifier1);
		StackLogger.pushInvocation("method1");
		assertEquals(testcaseIdentifier1, StackLogger.getCurrentTestCaseIdentifier());

		TestcaseIdentifier testcaseIdentifier2 = TestcaseIdentifier
				.parse("CommonTest" + TestcaseIdentifier.SEPARATOR + "testcase2");
		StackLogger.startLog(testcaseIdentifier2);
		assertTrue(StackLogger.getInvocationsMinDistance().isEmpty());
		assertEquals(testcaseIdentifier2, StackLogger.getCurrentTestCaseIdentifier());
	}

	@Test
	public void testPushInvocation() {
		TestcaseIdentifier testcaseIdentifier = TestcaseIdentifier
				.parse("CommonTest" + TestcaseIdentifier.SEPARATOR + "testcase1");
		MethodIdentifier methodIdentifier1 = MethodIdentifier.create("X", "method1",
				BytecodeConstants.DESCRIPTOR_NO_PARAM_AND_VOID);
		MethodIdentifier methodIdentifier2 = MethodIdentifier.create("X", "method2",
				BytecodeConstants.DESCRIPTOR_NO_PARAM_AND_VOID);
		MethodIdentifier methodIdentifier3 = MethodIdentifier.create("X", "method3",
				BytecodeConstants.DESCRIPTOR_NO_PARAM_AND_VOID);

		StackLogger.startLog(testcaseIdentifier);
		StackLogger.pushInvocation(methodIdentifier1.get());
		StackLogger.pushInvocation(methodIdentifier2.get());
		StackLogger.pushInvocation(methodIdentifier3.get());
		StackLogger.popInvocation();
		StackLogger.popInvocation();
		StackLogger.pushInvocation(methodIdentifier3.get());
		StackLogger.popInvocation();
		StackLogger.popInvocation();

		Map<MethodIdentifier, Integer> invocationsMinDistance = StackLogger.getInvocationsMinDistance();
		Map<MethodIdentifier, Integer> invocationsMaxDistance = StackLogger.getInvocationsMaxDistance();

		assertEquals(2, (int) invocationsMaxDistance.get(methodIdentifier2));
		assertEquals(3, (int) invocationsMaxDistance.get(methodIdentifier3));

		assertEquals(2, (int) invocationsMinDistance.get(methodIdentifier2));
		assertEquals(2, (int) invocationsMinDistance.get(methodIdentifier3));
	}
}
