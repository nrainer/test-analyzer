package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.collection;

import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogger;
import de.tum.in.niedermr.ta.runner.execution.infocollection.AbstractInformationCollectionLogic;

/** Logic to collect information about the test cases and methods under test. */
public class AnalysisInformationCollectionLogic extends AbstractInformationCollectionLogic {

	private final IResultPresentationExtended m_resultPresentation;
	private IResultReceiver m_resultReceiver;

	/** Constructor. */
	public AnalysisInformationCollectionLogic(IFullExecutionId executionId) {
		super(executionId);
		m_resultPresentation = IResultPresentationExtended.create(executionId);
	}

	@Override
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		m_resultReceiver = ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(isUseMultiFileOutput(),
				getOutputFile());
	}

	/** {@inheritDoc} */
	@Override
	protected void execBeforeExecutingTestcase(TestcaseIdentifier testCaseIdentifier) {
		StackLogger.startLog(testCaseIdentifier);
	}

	/** {@inheritDoc} */
	@Override
	protected void execTestcaseExecutedSuccessfully(TestcaseIdentifier testCaseIdentifier) {
		appendToResult(testCaseIdentifier, StackLogger.getInvocationsMinDistance(),
				StackLogger.getInvocationsMaxDistance(), StackLogger.getInvocationsCount());
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
