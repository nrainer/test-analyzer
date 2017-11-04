package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import de.tum.in.niedermr.ta.core.analysis.AbstractBytecodeMutationTest;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.BiasedTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.visitor.BytecodeModificationTestUtility;

public abstract class AbstractStackDistanceInstrumentationTest
		extends AbstractBytecodeMutationTest<StackDistanceSampleClass> {

	/** Constructor. */
	public AbstractStackDistanceInstrumentationTest() {
		super(StackDistanceSampleClass.class);
	}

	/** {@inheritDoc} */
	@Override
	protected Class<?> modifyClass(Class<?> classToBeModified) throws Exception {
		ICodeModificationOperation modificationOperation = new AnalysisInstrumentationOperation(
				new BiasedTestClassDetector(ClassType.NO_TEST_CLASS), getStackLogRecorder());
		return BytecodeModificationTestUtility.createAndLoadModifiedClass(classToBeModified, modificationOperation);
	}

	protected abstract Class<?> getStackLogRecorder();

	/** {@inheritDoc} */
	@Override
	protected final void verifyModification(Class<?> modifiedClass, Object instanceOfModifiedClass,
			StackDistanceSampleClass instanceOfOriginalClass) throws Exception {
		beforeVerifyModification();

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "empty");
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "returnMethodResult");
		assertInvocationCounts(2);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "throwException");
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "throwExternallyCreatedException");
		assertInvocationCounts(2, true);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "computation");
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "multiExits", new Integer(100));
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "multiExits", new Integer(400));
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "multiExits", new Integer(80));
		assertInvocationCounts(2);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "synchronizedMultiExits", new Integer(80));
		assertInvocationCounts(2);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "failInputDependent", Boolean.TRUE);
		assertInvocationCounts(2);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "failInputDependent", Boolean.FALSE);
		assertInvocationCounts(2);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "recursive", new Integer(4));
		assertInvocationCounts(4);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "tryFinally");
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "throwCatchThrow");
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "synchronizedThrowCatchThrow1");
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "synchronizedThrowCatchThrow2");
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "failIfTrue", Boolean.TRUE);
		assertInvocationCounts(1);
		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "failIfTrue", Boolean.FALSE);
		assertInvocationCounts(1);

		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "tryCatch", new Integer(1));
		assertInvocationCounts(1);
		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "tryCatch", new Integer(2));
		assertInvocationCounts(1);
		resetRecorderAndInvokeMethodNoInvocationEx(instanceOfModifiedClass, "tryCatch", new Integer(3));
		assertInvocationCounts(1);

		execVerifyFurther(modifiedClass, instanceOfModifiedClass, instanceOfOriginalClass);
	}

	protected void beforeVerifyModification() throws Exception {
		// NOP
	}

	/**
	 * @param modifiedClass
	 * @param instanceOfModifiedClass
	 * @param instanceOfOriginalClass
	 */
	protected void execVerifyFurther(Class<?> modifiedClass, Object instanceOfModifiedClass,
			StackDistanceSampleClass instanceOfOriginalClass) throws Exception {
		// NOP
	}

	protected abstract void resetRecorderAndInvokeMethodNoInvocationEx(Object instanceOfMutatedClass, String methodName,
			Object... params) throws ReflectiveOperationException;

	/** Assert the invocations. */
	protected final void assertInvocationCounts(int invocationCount) {
		assertInvocationCounts(invocationCount, true);
	}

	/** Assert the invocations. */
	protected abstract void assertInvocationCounts(int invocationCount, boolean skipMaxMethodNestingDepthCheck);
}
