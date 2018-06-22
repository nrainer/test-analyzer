package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

import java.io.IOException;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.common.TestUtility;

/** Test {@link TeamscaleSurefireTestListener}. */
public class TeamscaleSurefireTestListenerTest extends AbstractSurefireTestListenerTest {

	/** {@inheritDoc} */
	@Override
	protected AbstractSurefireTestListener createListenerInstance() {
		return new TeamscaleSurefireTestListener();
	}

	/** Test. */
	@Test
	public void testOutputGeneration2() throws IOException {
		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();

		AbstractSurefireTestListener listener = createListenerInstance();

		TestcaseIdentifier testcaseIdentifier1 = TestcaseIdentifier.create("SampleTest", "test1");
		TestcaseIdentifier testcaseIdentifier2 = TestcaseIdentifier.create("SampleTest", "test2");
		MethodIdentifier methodIdentifier = MethodIdentifier.create("java.lang.String", "toString",
				"()Ljava/lang/String;");

		listener.execAfterOutputWriterInitialized(resultReceiver);
		listener.writeCommentToResultFile(resultReceiver, "abc");
		listener.execBeforeAllTests(resultReceiver);
		listener.appendToResult(resultReceiver, testcaseIdentifier1, methodIdentifier, 3, 4);
		listener.appendToResult(resultReceiver, testcaseIdentifier2, methodIdentifier, 1, 6);
		listener.execAfterAllTests(resultReceiver);

		TestUtility.assertFileContentMatchesResultReceiver(getClass(), "expected-output-2.txt", resultReceiver);
	}
}
