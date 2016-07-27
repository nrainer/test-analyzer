package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/** Execution step to write the result to a file. */
public class PersistResultStep extends AbstractExecutionStep {
	private static final String DB_INSERT_METHOD_INFO = "INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('%s', '%s', %s, %s, '%s');";
	private static final String DB_INSERT_TESTCASE_INFO = "INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('%s', '%s', %s, %s, '%s');";
	private static final String VALUE_NAME_ASSERTIONS = "assertions";
	private static final String VALUE_NAME_MODIFIER = "modifier";
	private static final String VALUE_NAME_INSTRUCTIONS = "instructions";

	private static final String RESULT_FILE = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "code-statistics"
			+ FILE_EXTENSION_SQL_TXT;

	private final List<String> m_result = new LinkedList<>();

	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {
		TextFileData.writeToFile(getFileInWorkingArea(RESULT_FILE), m_result);
	}

	public void addResultInstructionsPerMethod(Map<MethodIdentifier, Integer> codeInformation) {
		addResultInternal(DB_INSERT_METHOD_INFO, codeInformation, null, VALUE_NAME_INSTRUCTIONS);
	}

	public void addResultModifierPerMethod(Map<MethodIdentifier, String> codeInformation) {
		addResultInternal(DB_INSERT_METHOD_INFO, null, codeInformation, VALUE_NAME_MODIFIER);
	}

	public void addResultInstructionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		addResultInternal(DB_INSERT_TESTCASE_INFO, codeInformation, null, VALUE_NAME_INSTRUCTIONS);
	}

	public void addResultAssertionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		addResultInternal(DB_INSERT_TESTCASE_INFO, codeInformation, null, VALUE_NAME_ASSERTIONS);
	}

	private void addResultInternal(String genericSqlStatement, Map<MethodIdentifier, Integer> intValueInformation,
			Map<MethodIdentifier, String> stringValueInformation, String valueName) {
		List<String> convertedData = convertToSqlStatements(genericSqlStatement, intValueInformation,
				stringValueInformation, valueName);
		this.m_result.addAll(convertedData);
	}

	private List<String> convertToSqlStatements(String genericSqlStatement,
			Map<MethodIdentifier, Integer> intValueInformation, Map<MethodIdentifier, String> stringValueInformation,
			String valueName) {
		List<String> sqlStatements = new LinkedList<>();

		if (intValueInformation != null) {
			for (Entry<MethodIdentifier, Integer> methodData : intValueInformation.entrySet()) {
				sqlStatements.add(createSqlStatement(genericSqlStatement, methodData.getKey(), methodData.getValue(),
						null, valueName));
			}
		}

		if (stringValueInformation != null) {
			for (Entry<MethodIdentifier, String> methodData : stringValueInformation.entrySet()) {
				sqlStatements.add(createSqlStatement(genericSqlStatement, methodData.getKey(), null,
						methodData.getValue(), valueName));
			}
		}

		return sqlStatements;
	}

	private String createSqlStatement(String genericSqlStatement, MethodIdentifier methodIdentifier, Integer intValue,
			String stringValue, String valueName) {
		String intValueAsString = intValue == null ? "NULL" : "'" + intValue.toString() + "'";
		String stringValueAsString = stringValue == null ? "NULL" : "'" + stringValue.toString() + "'";
		return String.format(genericSqlStatement, getExecutionId(), methodIdentifier.get(), intValueAsString,
				stringValueAsString, valueName);
	}

	@Override
	protected String getDescription() {
		return "Persisting the result in a single file";
	}
}
