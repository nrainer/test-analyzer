package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.tum.in.niedermr.ta.core.analysis.AbstractBytecodeMutationTest;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.BiasedTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.visitor.BytecodeModificationTestUtility;

public class StackDistanceInstrumentationTest extends AbstractBytecodeMutationTest<StackDistanceSampleClass> {

	/** Constructor. */
	public StackDistanceInstrumentationTest() {
		super(StackDistanceSampleClass.class);
	}

	/** {@inheritDoc} */
	@Override
	protected Class<?> mutateClass(Class<?> classToBeMutated) throws Exception {
		ICodeModificationOperation modificationOperation = new AnalysisInstrumentationOperation(
				new BiasedTestClassDetector(ClassType.NO_TEST_CLASS), StackLogRecorderForTestingPurposes.class);
		return BytecodeModificationTestUtility.createAndLoadModifiedClass(classToBeMutated, modificationOperation);
	}

	/** {@inheritDoc} */
	@Override
	protected void verifyMutation(Class<?> mutatedClass, Object instanceOfMutatedClass,
			StackDistanceSampleClass instanceOfOriginalClass) throws Exception {

		StackLogRecorderForTestingPurposes.reset();
		invokeMethod(instanceOfMutatedClass, "empty");
		assertInvocationCounts(1, 1);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethod(instanceOfMutatedClass, "returnMethodResult");
		assertInvocationCounts(2, 2);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfMutatedClass, "throwException");
		// assertInvocationCounts(1, 1);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethod(instanceOfMutatedClass, "computation");
		assertInvocationCounts(1, 1);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfMutatedClass, "failInputDependent", Boolean.TRUE);
		assertInvocationCounts(2, 2);
		assertTrue(StackLogRecorderForTestingPurposes.s_methodIdentifierStrings.get(0).contains("failInputDependent"));

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfMutatedClass, "failInputDependent", Boolean.FALSE);
		assertInvocationCounts(2, 2);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfMutatedClass, "tryFinally");
		assertInvocationCounts(1, 1);
	}

	protected void assertInvocationCounts(int pushInvocations, int popInvocations) {
		assertEquals("Push invocation mismatch", pushInvocations,
				StackLogRecorderForTestingPurposes.s_pushInvocationCount);
		assertEquals("Pop invocation mismatch", popInvocations,
				StackLogRecorderForTestingPurposes.s_popInvocationCount);
	}

}
