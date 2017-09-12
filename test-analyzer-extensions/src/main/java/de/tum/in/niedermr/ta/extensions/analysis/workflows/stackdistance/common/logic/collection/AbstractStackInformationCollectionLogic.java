package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;
import de.tum.in.niedermr.ta.runner.execution.infocollection.AbstractInformationCollectionLogic;

/** Logic to collect information about the test cases and methods under test. */
public abstract class AbstractStackInformationCollectionLogic extends AbstractInformationCollectionLogic {

	private IResultPresentationExtended m_resultPresentation;
	private IResultReceiver m_resultReceiver;

	@Override
	public void setExecutionId(IFullExecutionId executionId) {
		super.setExecutionId(executionId);
		m_resultPresentation = IResultPresentationExtended.create(executionId);
	}

	@Override
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		Objects.requireNonNull(m_resultPresentation);
		m_resultReceiver = ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(isUseMultiFileOutput(),
				getOutputFile());
	}

	/** {@inheritDoc} */
	@Override
	protected void execBeforeExecutingTestcase(TestcaseIdentifier testCaseIdentifier) {
		startStackLogRecorder(testCaseIdentifier);
	}

	/** Start the stack logger. */
	protected abstract void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier);

	/** {@inheritDoc} */
	@Override
	protected void execTestcaseExecutedSuccessfully(TestcaseIdentifier testCaseIdentifier) {
		super.execTestcaseExecutedSuccessfully(testCaseIdentifier);
		appendStackDistanceOfTestcaseToResult(testCaseIdentifier);
	}

	/** {@inheritDoc} */
	@Override
	protected void execTestcaseExecutedWithFailure(TestcaseIdentifier testCaseIdentifier, ITestRunResult testResult) {
		super.execTestcaseExecutedWithFailure(testCaseIdentifier, testResult);

		if (isIncludeFailingTests()) {
			appendStackDistanceOfTestcaseToResult(testCaseIdentifier);
		}
	}

	protected void appendStackDistanceOfTestcaseToResult(TestcaseIdentifier testCaseIdentifier) {
		appendToResult(testCaseIdentifier, StackLogDataManager.getInvocationsMinDistance(),
				StackLogDataManager.getInvocationsMaxDistance(), StackLogDataManager.getInvocationsCount());
	}

	protected void appendToResult(TestcaseIdentifier testCaseIdentifier,
			Map<MethodIdentifier, Integer> invocationMinDistances,
			Map<MethodIdentifier, Integer> invocationMaxDistances, Map<MethodIdentifier, Integer> invocationsCount) {
		for (MethodIdentifier invokedMethod : invocationMinDistances.keySet()) {
			int minInvocationDistance = invocationMinDistances.get(invokedMethod);
			int maxInvocationDistance = invocationMaxDistances.get(invokedMethod);
			int invocationCount = invocationsCount.get(invokedMethod);

			m_resultReceiver.append(m_resultPresentation.formatStackDistanceInfoEntry(testCaseIdentifier, invokedMethod,
					minInvocationDistance, maxInvocationDistance, invocationCount));
		}

		m_resultReceiver.markResultAsPartiallyComplete();
	}

	/** {@inheritDoc} */
	@Override
	protected void execAllTestsExecuted(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		m_resultReceiver.markResultAsComplete();
	}
}
