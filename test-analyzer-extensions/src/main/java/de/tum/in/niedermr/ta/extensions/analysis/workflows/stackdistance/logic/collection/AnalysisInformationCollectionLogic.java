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
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogger;
import de.tum.in.niedermr.ta.runner.execution.infocollection.AbstractInformationCollectionLogic;

public class AnalysisInformationCollectionLogic extends AbstractInformationCollectionLogic {
	private static final Logger LOG = LogManager.getLogger(AnalysisInformationCollectionLogic.class);

	private static final String SQL_INSERT_STACK_INFO = "INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('%s', '%s', '%s', %s, %s);";

	private final List<String> m_result;

	public AnalysisInformationCollectionLogic(IFullExecutionId executionId) {
		super(executionId);
		m_result = new LinkedList<>();
	}

	@Override
	protected void execBeforeExecutingTestcase(TestcaseIdentifier testCaseIdentifier) {
		StackLogger.startLog(testCaseIdentifier);
	}

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

			m_result.add(String.format(SQL_INSERT_STACK_INFO, getExecutionId().getShortId(),
					testCaseIdentifier.toMethodIdentifier().get(), invokedMethod.get(), minInvocationDistance,
					maxInvocationDistance));
		}
	}

	@Override
	protected void execAllTestsExecuted(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		try {
			writeResultsToFile();
		} catch (IOException ex) {
			LOG.error("when writing results to file", ex);
		}
	}

	protected void writeResultsToFile() throws IOException {
		TextFileData.writeToFile(getOutputFile(), m_result);
	}
}
