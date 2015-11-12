package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

public class PersistResultStep extends AbstractExecutionStep {
	private static final String DB_INSERT_INSTRUCTIONS_PER_METHOD = "INSERT INTO Method_Instructions (method, countInstructions) VALUES ('%s', '%s');";
	private static final String DB_INSERT_INSTRUCTIONS_PER_TESTCASE = "INSERT INTO Testcase_Instructions (testcase, countInstructions) VALUES ('%s', '%s');";
	private static final String DB_INSERT_ASSERTIONS_PER_TESTCASE = "INSERT INTO Testcase_Assertions (testcase, countAssertions) VALUES ('%s', '%s');";

	private static final String RESULT_FILE = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "code-statistics" + FILE_EXTENSION_SQL_TXT;

	private final List<String> m_result;

	public PersistResultStep(ExecutionInformation information) {
		super(information);

		this.m_result = new LinkedList<>();
	}

	@Override
	protected void runInternal() throws Throwable {
		TextFileData.writeToFile(getFileInWorkingArea(RESULT_FILE), m_result);
	}

	public void addResultInstructionsPerMethod(Map<MethodIdentifier, Integer> codeInformation) {
		addResultInternal(DB_INSERT_INSTRUCTIONS_PER_METHOD, codeInformation);
	}

	public void addResultInstructionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		addResultInternal(DB_INSERT_INSTRUCTIONS_PER_TESTCASE, codeInformation);
	}

	public void addResultAssertionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		addResultInternal(DB_INSERT_ASSERTIONS_PER_TESTCASE, codeInformation);
	}

	private void addResultInternal(String genericSqlStatement, Map<MethodIdentifier, Integer> codeInformation) {
		List<String> convertedData = convertToSqlStatements(genericSqlStatement, codeInformation);
		this.m_result.addAll(convertedData);
	}

	private List<String> convertToSqlStatements(String genericSqlStatement, Map<MethodIdentifier, Integer> codeInformation) {
		List<String> sqlStatements = new LinkedList<>();

		for (Entry<MethodIdentifier, Integer> methodData : codeInformation.entrySet()) {
			sqlStatements.add(String.format(genericSqlStatement, methodData.getKey().get(), methodData.getValue()));
		}

		return sqlStatements;
	}

	@Override
	protected String getDescription() {
		return "Persisting the result in a single file";
	}
}
