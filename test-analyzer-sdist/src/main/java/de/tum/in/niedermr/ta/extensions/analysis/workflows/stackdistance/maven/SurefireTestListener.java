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
public class SurefireTestListener extends RunListener {

	private static final String OUTPUT_FILE = "./stack-distance.sql";

	/**
	 * Note that for parameterized test cases more than one statement may be created for a certain test case identifier.
	 */
	private static final String SQL_INSERT_STACK_DISTANCE_STATEMENT = "INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, invocationCount) VALUES ('????', '%s', '%s', '%s', '%s'); ";

	private AbstractThreadAwareStackDistanceManager m_stackDistanceManager;
	private IResultReceiver m_resultReceiver;
	private transient boolean m_currentTestcaseFailed = false;

	/** {@inheritDoc} */
	@Override
	public void testRunStarted(Description description) throws Exception {
		ensureOutputWriterInitialized();

		try {
			m_stackDistanceManager = new ThreadAwareStackDistanceManagerV3();
			m_stackDistanceManager.beforeAllTests();
			m_resultReceiver.append("# INFO Stack distance setup successful.");
			m_resultReceiver.markResultAsPartiallyComplete();
		} catch (Exception e) {
			m_resultReceiver.append("# ERROR Stack distance setup failed!");
			m_resultReceiver.append("# Cause is: " + e.getMessage());
			m_resultReceiver.append(
					"# Check if the endorsed dir is used. It must be specified explicitly in the surefire configuration!");
			m_resultReceiver.markResultAsPartiallyComplete();
			throw e;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void testRunFinished(Result result) throws Exception {
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
			return;
		}

		appendStackDistanceOfTestcaseToResult(createTestcaseIdentifier(description));
	}

	private void ensureOutputWriterInitialized() {
		if (m_resultReceiver != null) {
			return;
		}

		m_resultReceiver = ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(false, OUTPUT_FILE);
	}

	private void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		m_stackDistanceManager.startStackLogger(testCaseIdentifier);
	}

	private TestcaseIdentifier createTestcaseIdentifier(Description description) {
		return TestcaseIdentifier.create(description.getTestClass(), description.getMethodName());
	}

	private void appendStackDistanceOfTestcaseToResult(TestcaseIdentifier testCaseIdentifier) {
		appendToResult(testCaseIdentifier, StackLogDataManager.getInvocationsMinDistance(),
				StackLogDataManager.getInvocationsCount());
	}

	private void appendToResult(TestcaseIdentifier testCaseIdentifier,
			Map<MethodIdentifier, Integer> invocationMinDistances, Map<MethodIdentifier, Integer> invocationsCount) {
		for (MethodIdentifier methodUnderTest : invocationMinDistances.keySet()) {
			int minInvocationDistance = invocationMinDistances.get(methodUnderTest);
			int invocationCount = invocationsCount.get(methodUnderTest);

			String sqlStatement = String.format(SQL_INSERT_STACK_DISTANCE_STATEMENT, testCaseIdentifier,
					methodUnderTest, minInvocationDistance, invocationCount);
			m_resultReceiver.append(sqlStatement);
		}

		m_resultReceiver.markResultAsPartiallyComplete();
	}
}
