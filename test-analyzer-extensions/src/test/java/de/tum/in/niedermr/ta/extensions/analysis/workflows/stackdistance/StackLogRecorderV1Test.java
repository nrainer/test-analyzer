package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

/** Test {@link StackLogRecorderV1}. */
public class StackLogRecorderV1Test {

	/** Test. */
	@Test
	public void testStartLog() {
		TestcaseIdentifier testcaseIdentifier1 = TestcaseIdentifier
				.parse("CommonTest" + TestcaseIdentifier.SEPARATOR + "testcase1");
		StackLogRecorderV1.startLog(testcaseIdentifier1);
		StackLogRecorderV1.pushInvocation("method1");
		assertEquals(testcaseIdentifier1, StackLogDataManager.getCurrentTestCaseIdentifier());

		TestcaseIdentifier testcaseIdentifier2 = TestcaseIdentifier
				.parse("CommonTest" + TestcaseIdentifier.SEPARATOR + "testcase2");
		StackLogRecorderV1.startLog(testcaseIdentifier2);
		assertTrue(StackLogDataManager.getInvocationsMinDistance().isEmpty());
		assertEquals(testcaseIdentifier2, StackLogDataManager.getCurrentTestCaseIdentifier());
	}

	/** Test. */
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

		StackLogRecorderV1.startLog(testcaseIdentifier);
		StackLogRecorderV1.pushInvocation(methodIdentifier1.get());
		StackLogRecorderV1.pushInvocation(methodIdentifier2.get());
		StackLogRecorderV1.pushInvocation(methodIdentifier3.get());
		StackLogRecorderV1.popInvocation();
		StackLogRecorderV1.popInvocation();
		StackLogRecorderV1.pushInvocation(methodIdentifier3.get());
		StackLogRecorderV1.popInvocation();
		StackLogRecorderV1.popInvocation();

		Map<MethodIdentifier, Integer> invocationsMinDistance = StackLogDataManager.getInvocationsMinDistance();
		Map<MethodIdentifier, Integer> invocationsMaxDistance = StackLogDataManager.getInvocationsMaxDistance();
		Map<MethodIdentifier, Integer> invocationsCount = StackLogDataManager.getInvocationsCount();

		assertEquals(2, (int) invocationsMaxDistance.get(methodIdentifier2));
		assertEquals(3, (int) invocationsMaxDistance.get(methodIdentifier3));

		assertEquals(2, (int) invocationsMinDistance.get(methodIdentifier2));
		assertEquals(2, (int) invocationsMinDistance.get(methodIdentifier3));

		assertEquals(1, (int) invocationsCount.get(methodIdentifier1));
		assertEquals(1, (int) invocationsCount.get(methodIdentifier2));
		assertEquals(2, (int) invocationsCount.get(methodIdentifier3));
	}
}
