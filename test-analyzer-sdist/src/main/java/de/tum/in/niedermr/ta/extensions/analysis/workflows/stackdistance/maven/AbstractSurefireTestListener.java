package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.AbstractThreadAwareStackDistanceManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.StackLogDataManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v3.ThreadAwareStackDistanceManagerV3;

/**
 * Test listener for Maven (JUnit) tests. <br/>
 * Uses the logic of {@link StackDistanceAnalysisWorkflowV3}. <br/>
 * 
 * This listener records and writes the stack distance to a file. Note that the instrumentation must be done before
 * running the tests with this listener.
 */
public abstract class AbstractSurefireTestListener extends RunListener {

	private AbstractThreadAwareStackDistanceManager m_stackDistanceManager;
	private IResultReceiver m_resultReceiver;
	private transient boolean m_currentTestcaseFailed = false;

	/** {@link #m_resultReceiver} */
	public void setResultReceiver(IResultReceiver resultReceiver) {
		m_resultReceiver = resultReceiver;
	}

	/** {@link #m_resultReceiver} */
	public IResultReceiver getResultReceiver() {
		return m_resultReceiver;
	}

	/** {@inheritDoc} */
	@Override
	public final void testRunStarted(Description description) throws Exception {
		ensureOutputWriterInitialized();
		execAfterOutputWriterInitialized(m_resultReceiver);

		try {
			m_stackDistanceManager = new ThreadAwareStackDistanceManagerV3();
			m_stackDistanceManager.beforeAllTests();
			writeCommentToResultFile("INFO Stack distance setup successful.");
			m_resultReceiver.markResultAsPartiallyComplete();
		} catch (IllegalStateException e) {
			writeCommentToResultFile("ERROR Stack distance setup failed!");
			writeCommentToResultFile("Cause is: " + e.getMessage());
			writeCommentToResultFile(
					"Check if the endorsed dir is used. It must be specified explicitly in the surefire configuration!");
			m_resultReceiver.markResultAsPartiallyComplete();
			throw e;
		}

		execBeforeAllTests(m_resultReceiver);
	}

	/**
	 * @param resultReceiver
	 */
	protected void execBeforeAllTests(IResultReceiver resultReceiver) {
		// NOP
	}

	/**
	 * @param resultReceiver
	 */
	protected void execAfterAllTests(IResultReceiver resultReceiver) {
		// NOP
	}

	/**
	 * @param resultReceiver
	 */
	protected void execAfterOutputWriterInitialized(IResultReceiver resultReceiver) {
		// NOP
	}

	protected final void writeCommentToResultFile(String comment) {
		writeCommentToResultFile(m_resultReceiver, comment);
	}

	protected abstract void writeCommentToResultFile(IResultReceiver resultReceiver, String comment);

	protected String getDefaultOutputFilePath() {
		return "./target/stack-distance/sdist_" + CommonUtility.createDateTimeStringForFile()
				+ getDefaultOutputFileExtension();
	}

	protected abstract String getDefaultOutputFileExtension();

	/** {@inheritDoc} */
	@Override
	public final void testRunFinished(Result result) throws Exception {
		execAfterAllTests(m_resultReceiver);
		m_resultReceiver.markResultAsComplete();
	}

	/** {@inheritDoc} */
	@Override
	public void testStarted(Description description) throws Exception {
		m_currentTestcaseFailed = false;
		startStackLogRecorder(createTestcaseIdentifier(description));
	}

	/** {@inheritDoc} */
	@Override
	public void testFailure(Failure failure) throws Exception {
		m_currentTestcaseFailed = true;
	}

	/** {@inheritDoc} */
	@Override
	public void testAssumptionFailure(Failure failure) {
		m_currentTestcaseFailed = true;
	}

	/** {@inheritDoc} */
	@Override
	public void testFinished(Description description) throws Exception {
		if (m_currentTestcaseFailed) {
			writeCommentToResultFile("Failing test case: " + createTestcaseIdentifier(description).get());
			return;
		}

		appendStackDistanceOfTestcaseToResult(createTestcaseIdentifier(description));
	}

	private void ensureOutputWriterInitialized() {
		if (m_resultReceiver != null) {
			return;
		}

		m_resultReceiver = ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(false,
				getDefaultOutputFilePath());
	}

	private void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		m_stackDistanceManager.startStackLogger(testCaseIdentifier);
	}

	private TestcaseIdentifier createTestcaseIdentifier(Description description) {
		return TestcaseIdentifier.create(description.getTestClass(), description.getMethodName());
	}

	private void appendStackDistanceOfTestcaseToResult(TestcaseIdentifier testCaseIdentifier) {
		appendToResult(m_resultReceiver, testCaseIdentifier, StackLogDataManager.getInvocationsMinDistance(),
				StackLogDataManager.getInvocationsCount());
	}

	protected void appendToResult(IResultReceiver resultReceiver, TestcaseIdentifier testCaseIdentifier,
			Map<MethodIdentifier, Integer> invocationMinDistances, Map<MethodIdentifier, Integer> invocationsCount) {
		for (MethodIdentifier methodUnderTest : invocationMinDistances.keySet()) {
			int minInvocationDistance = invocationMinDistances.get(methodUnderTest);
			int invocationCount = invocationsCount.get(methodUnderTest);

			appendToResult(resultReceiver, testCaseIdentifier, methodUnderTest, minInvocationDistance, invocationCount);
		}

		resultReceiver.markResultAsPartiallyComplete();
	}

	protected abstract void appendToResult(IResultReceiver resultReceiver, TestcaseIdentifier testCaseIdentifier,
			MethodIdentifier methodUnderTest, int minInvocationDistance, int invocationCount);
}
