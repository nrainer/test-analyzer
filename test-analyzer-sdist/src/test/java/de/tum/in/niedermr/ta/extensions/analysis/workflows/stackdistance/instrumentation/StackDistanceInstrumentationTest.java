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
	protected Class<?> modifyClass(Class<?> classToBeModified) throws Exception {
		ICodeModificationOperation modificationOperation = new AnalysisInstrumentationOperation(
				new BiasedTestClassDetector(ClassType.NO_TEST_CLASS), StackLogRecorderForTestingPurposes.class);
		return BytecodeModificationTestUtility.createAndLoadModifiedClass(classToBeModified, modificationOperation);
	}

	/** {@inheritDoc} */
	@Override
	protected void verifyModification(Class<?> modifiedClass, Object instanceOfModifiedClass,
			StackDistanceSampleClass instanceOfOriginalClass) throws Exception {

		StackLogRecorderForTestingPurposes.reset();
		invokeMethod(instanceOfModifiedClass, "empty");
		assertInvocationCounts(1, 1);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethod(instanceOfModifiedClass, "returnMethodResult");
		assertInvocationCounts(2, 2);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfModifiedClass, "throwException");
		// assertInvocationCounts(1, 1);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethod(instanceOfModifiedClass, "computation");
		assertInvocationCounts(1, 1);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfModifiedClass, "failInputDependent", Boolean.TRUE);
		assertInvocationCounts(2, 2);
		assertTrue(StackLogRecorderForTestingPurposes.s_methodIdentifierStrings.get(0).contains("failInputDependent"));

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfModifiedClass, "failInputDependent", Boolean.FALSE);
		assertInvocationCounts(2, 2);

		StackLogRecorderForTestingPurposes.reset();
		invokeMethodNoInvocationEx(instanceOfModifiedClass, "tryFinally");
		assertInvocationCounts(1, 1);
	}

	protected void assertInvocationCounts(int pushInvocations, int popInvocations) {
		assertEquals("Push invocation mismatch", pushInvocations,
				StackLogRecorderForTestingPurposes.s_pushInvocationCount);
		assertEquals("Pop invocation mismatch", popInvocations,
				StackLogRecorderForTestingPurposes.s_popInvocationCount);
	}

}
