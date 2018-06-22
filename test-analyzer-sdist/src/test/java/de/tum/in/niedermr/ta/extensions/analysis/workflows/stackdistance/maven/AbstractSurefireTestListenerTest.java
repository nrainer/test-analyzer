package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v2.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v3.StackLogRecorderV3;
import de.tum.in.niedermr.ta.sample.SampleClass;
import de.tum.in.niedermr.ta.sample.junit.SampleJUnitTestClass;

public abstract class AbstractSurefireTestListenerTest {

	private static final MethodIdentifier METHOD_ID_1 = MethodIdentifier.create(SampleClass.class, "a", "()V");
	private static final MethodIdentifier METHOD_ID_2 = MethodIdentifier.create(SampleClass.class, "a", "(II)V");
	private static final MethodIdentifier METHOD_ID_3 = MethodIdentifier.create(SampleClass.class, "b", "(Z)I");
	private static final MethodIdentifier METHOD_ID_4 = MethodIdentifier.create(SampleClass.class, "c",
			"(Ljava/lang/String;)Z");

	@BeforeClass
	public static void setUp() {
		ThreadStackManager.disableThreadClassVerification();
	}

	@AfterClass
	public static void tearDown() {
		ThreadStackManager.enableThreadClassVerification();
	}

	@Test
	public void testOutputGeneration() throws Exception {
		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();

		AbstractSurefireTestListener listener = createListenerInstance();
		listener.setResultReceiver(resultReceiver);

		listener.testRunStarted(Description.createSuiteDescription(SampleJUnitTestClass.class));
		simulateTestCaseExecution1(listener);
		simulateTestCaseExecution2(listener);
		listener.testRunFinished(new Result());

		TestUtility.assertFileContentMatchesResultReceiver(getClass(), "expected-output.txt", resultReceiver);
	}

	private void simulateTestCaseExecution1(AbstractSurefireTestListener listener) throws Exception {
		Description testDescription1 = Description.createTestDescription(SampleJUnitTestClass.class, "testCase1");
		listener.testStarted(testDescription1);
		StackLogRecorderV3.pushInvocation(METHOD_ID_1.get());
		StackLogRecorderV3.pushInvocation(METHOD_ID_2.get());
		StackLogRecorderV3.pushInvocation(METHOD_ID_3.get());
		StackLogRecorderV3.pushInvocation(METHOD_ID_4.get());
		StackLogRecorderV3.pushInvocation(METHOD_ID_4.get());
		StackLogRecorderV3.pushInvocation(METHOD_ID_1.get());
		StackLogRecorderV3.popInvocation(METHOD_ID_1.get());
		StackLogRecorderV3.popInvocation(METHOD_ID_4.get());
		StackLogRecorderV3.popInvocation(METHOD_ID_4.get());
		StackLogRecorderV3.popInvocation(METHOD_ID_3.get());
		StackLogRecorderV3.popInvocation(METHOD_ID_2.get());
		StackLogRecorderV3.popInvocation(METHOD_ID_1.get());
		listener.testFinished(testDescription1);
	}

	private void simulateTestCaseExecution2(AbstractSurefireTestListener listener) throws Exception {
		Description testDescription2 = Description.createTestDescription(SampleJUnitTestClass.class, "testCase2");
		listener.testStarted(testDescription2);
		StackLogRecorderV3.pushInvocation(METHOD_ID_1.get());
		StackLogRecorderV3.pushInvocation(METHOD_ID_1.get());
		StackLogRecorderV3.pushInvocation(METHOD_ID_2.get());
		StackLogRecorderV3.popInvocation(METHOD_ID_1.get());
		listener.testFailure(new Failure(testDescription2, new AssertionError()));
		listener.testFinished(testDescription2);
	}

	protected abstract AbstractSurefireTestListener createListenerInstance();
}
