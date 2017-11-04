package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import static org.junit.Assert.assertEquals;

public class StackDistanceInstrumentationWithDummyRecorderTest extends AbstractStackDistanceInstrumentationTest {

	/** {@inheritDoc} */
	@Override
	protected void resetRecorderAndInvokeMethodNoInvocationEx(Object instanceOfMutatedClass, String methodName,
			Object... params) throws ReflectiveOperationException {
		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfMutatedClass, methodName, params);
	}

	/** {@inheritDoc} */
	@Override
	protected void assertInvocationCounts(int invocationCount, boolean skipMaxMethodNestingDepthCheck) {
		assertInvocationCounts(invocationCount, invocationCount);

		if (!skipMaxMethodNestingDepthCheck) {
			assertMaxInvocationCount(invocationCount);
		}
	}

	/** Assert the invocations. */
	protected void assertInvocationCounts(int pushInvocations, int popInvocations) {
		assertEquals("Push invocation mismatch", pushInvocations,
				StackLogRecorderForTestingPurposes.s_pushInvocationCount);
		assertEquals("Pop invocation mismatch", popInvocations,
				StackLogRecorderForTestingPurposes.s_popInvocationCount);
	}

	/** Assert the invocations. */
	protected void assertMaxInvocationCount(int maxMethodNestingDepth) {
		assertEquals("Max method nesting depth mismatch", maxMethodNestingDepth,
				StackLogRecorderForTestingPurposes.s_maxMethodNestingDepth);
	}

	/** {@inheritDoc} */
	@Override
	protected Class<?> getStackLogRecorder() {
		return StackLogRecorderForTestingPurposes.class;
	}
}
