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
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogger;
import de.tum.in.niedermr.ta.runner.execution.infocollection.AbstractInformationCollectionLogic;

public class AnalysisInformationCollectionLogic extends AbstractInformationCollectionLogic {
	private static final Logger LOG = LogManager.getLogger(AnalysisInformationCollectionLogic.class);

	private static final String SQL_CREATE_TABLE_STACK_INFO = "CREATE TABLE IF NOT EXISTS Stack_Info (execution VARCHAR(5), testcase VARCHAR(1024), method VARCHAR(1024), minStackDistance INT(8), maxStackDistance INT(8));";
	private static final String SQL_CREATE_INDEX_STACK_INFO_1 = "ALTER TABLE Stack_Info ADD INDEX (execution);";
	private static final String SQL_CREATE_INDEX_STACK_INFO_2 = "ALTER TABLE Stack_Info ADD INDEX (testcase(100));";
	private static final String SQL_CREATE_INDEX_STACK_INFO_3 = "ALTER TABLE Stack_Info ADD INDEX (method(100));";
	private static final String SQL_INSERT_STACK_INFO = "INSERT INTO Stack_Info (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('%s', '%s', '%s', %s, %s);";

	private final String m_shortExecutionId;
	private final List<String> m_result;
	private String m_outputFile;

	public AnalysisInformationCollectionLogic(String executionId) {
		super(executionId);
		m_shortExecutionId = getExecutionId().substring(0, CommonUtility.LENGTH_OF_RANDOM_ID);
		m_result = new LinkedList<>();
	}

	public String getOutputFile() {
		return m_outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.m_outputFile = outputFile;
	}

	@Override
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		m_result.add("-- Execution ID: " + m_shortExecutionId);
		m_result.add("-- " + SQL_CREATE_TABLE_STACK_INFO);
		m_result.add("-- " + SQL_CREATE_INDEX_STACK_INFO_1);
		m_result.add("-- " + SQL_CREATE_INDEX_STACK_INFO_2);
		m_result.add("-- " + SQL_CREATE_INDEX_STACK_INFO_3);
		m_result.add("-- -------");
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

			m_result.add(String.format(SQL_INSERT_STACK_INFO, m_shortExecutionId,
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
		TextFileData.writeToFile(m_outputFile, m_result);
	}
}
