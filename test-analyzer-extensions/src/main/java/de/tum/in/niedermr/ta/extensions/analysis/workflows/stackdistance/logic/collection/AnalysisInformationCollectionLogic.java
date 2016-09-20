package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.collection;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogger;
import de.tum.in.niedermr.ta.runner.execution.infocollection.AbstractInformationCollectionLogic;

/** Logic to collect information about the test cases and methods under test. */
public class AnalysisInformationCollectionLogic extends AbstractInformationCollectionLogic {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AnalysisInformationCollectionLogic.class);

	private final List<String> m_result;
	private final IResultPresentationExtended m_resultPresentation;

	/** Constructor. */
	public AnalysisInformationCollectionLogic(IFullExecutionId executionId) {
		super(executionId);
		m_result = new LinkedList<>();
		m_resultPresentation = IResultPresentationExtended.create(executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void execBeforeExecutingTestcase(TestcaseIdentifier testCaseIdentifier) {
		StackLogger.startLog(testCaseIdentifier);
	}

	/** {@inheritDoc} */
	@Override
	protected void execTestcaseExecutedSuccessfully(TestcaseIdentifier testCaseIdentifier) {
		addToResult(testCaseIdentifier, StackLogger.getInvocationsMinDistance(),
				StackLogger.getInvocationsMaxDistance());
	}

	protected void addToResult(TestcaseIdentifier testCaseIdentifier,
			Map<MethodIdentifier, Integer> invocationMinDistances,
			Map<MethodIdentifier, Integer> invocationMaxDistances) {
		for (MethodIdentifier invokedMethod : invocationMinDistances.keySet()) {
			int minInvocationDistance = invocationMinDistances.get(invokedMethod);
			int maxInvocationDistance = invocationMaxDistances.get(invokedMethod);

			m_result.add(m_resultPresentation.formatStackDistanceInfoEntry(testCaseIdentifier, invokedMethod,
					minInvocationDistance, maxInvocationDistance));
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execAllTestsExecuted(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		try {
			TextFileData.writeToFile(getOutputFile(), m_result);
		} catch (IOException ex) {
			LOGGER.error("when writing results to file", ex);
		}
	}
}
