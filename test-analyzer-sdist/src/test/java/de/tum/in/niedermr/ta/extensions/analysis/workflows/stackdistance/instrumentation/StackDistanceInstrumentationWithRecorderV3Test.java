package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import static org.junit.Assert.assertEquals;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.StackLogDataManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.threading.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v3.StackLogRecorderV3;

public class StackDistanceInstrumentationWithRecorderV3Test extends AbstractStackDistanceInstrumentationTest {

	/** {@inheritDoc} */
	@Override
	protected void beforeVerifyModification() throws Exception {
		StackLogRecorderV3.setThreadStackManagerAndVerify(new ThreadStackManager() {
			/** {@inheritDoc} */
			@Override
			public void verifyReplacedThreadClassInUse() {
				// NOP: not required for the test in this usecase
			}
		});
	}

	/** {@inheritDoc} */
	@Override
	protected void execVerifyFurther(Class<?> modifiedClass, Object instanceOfModifiedClass,
			StackDistanceSampleClass instanceOfOriginalClass) throws Exception {
		// pop invocation missing due to ATHROW situation (push, but not pop)
		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "failIfFalse", Boolean.TRUE);
		assertInvocationCounts(1);
		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "failIfFalse", Boolean.FALSE);
		assertInvocationCounts(1);

		// missing pop invocation gets fixed: delegateToDelegateToFailIfTrue fixes the counter after the missing pop
		// invocation in delegateToFailIfTrue due to the ATHROW
		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "delegateToDelegateToFailIfTrue");
		assertInvocationCounts(6);
	}

	/** {@inheritDoc} */
	@Override
	protected void resetRecorderAndInvokeMethodNoInvocationEx(Object instanceOfMutatedClass, String methodName,
			Object... params) throws ReflectiveOperationException {
		StackLogRecorderV3.startLog(TestcaseIdentifier.create(getClass(), "test"));
		invokeMethodNoInvocationEx(instanceOfMutatedClass, methodName, params);
	}

	/** {@inheritDoc} */
	@Override
	protected void assertInvocationCounts(int invocationCount, boolean skipMaxMethodNestingDepthCheck) {
		if (skipMaxMethodNestingDepthCheck) {
			return;
		}

		int actualMaxStackDepth = StackLogDataManager.getInvocationsMaxDistance().values().stream().reduce(Integer::max)
				.orElse(0);
		assertEquals("Max method nesting depth mismatch", invocationCount, actualMaxStackDepth);
	}

	/** {@inheritDoc} */
	@Override
	protected Class<?> getStackLogRecorder() {
		return StackLogRecorderV3.class;
	}
}
