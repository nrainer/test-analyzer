package de.tum.in.niedermr.ta.extensions.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageLevel;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageValueType;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.DatabaseResultPresentation;

/** An extended version of the database result presentation. */
public class ExtendedDatabaseResultPresentation extends DatabaseResultPresentation
		implements IResultPresentationExtended {

	private static final String SQL_INSERT_STACK_INFO_IMPORT = "INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('%s', '%s', '%s', %s, %s);";
	private static final String SQL_INSERT_METHOD_INFO_IMPORT = "INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('%s', '%s', %s, %s, '%s');";
	private static final String SQL_INSERT_TESTCASE_INFO_IMPORT = "INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('%s', '%s', %s, %s, '%s');";

	private static final String VALUE_NAME_INSTRUCTIONS = "instructions";
	private static final String VALUE_NAME_MODIFIER = "modifier";
	private static final String VALUE_NAME_ASSERTIONS = "assertions";

	private static final String NULL_VALUE = "NULL";

	/** {@inheritDoc} */
	@Override
	public String formatStackDistanceInfoEntry(TestcaseIdentifier testCaseIdentifier, MethodIdentifier methodUnderTest,
			int minInvocationDistance, int maxInvocationDistance) {
		return String.format(SQL_INSERT_STACK_INFO_IMPORT, getExecutionId().getShortId(),
				testCaseIdentifier.toMethodIdentifier().get(), methodUnderTest.get(), minInvocationDistance,
				maxInvocationDistance);
	}

	@Override
	public String formatInstructionsPerMethod(MethodIdentifier methodIdentifier, int instructionCount) {
		return String.format(SQL_INSERT_METHOD_INFO_IMPORT, getExecutionId().getShortId(), methodIdentifier.get(),
				inQuotes(instructionCount), NULL_VALUE, VALUE_NAME_INSTRUCTIONS);
	}

	@Override
	public String formatModifierPerMethod(MethodIdentifier methodIdentifier, String modifier) {
		return String.format(SQL_INSERT_METHOD_INFO_IMPORT, getExecutionId().getShortId(), methodIdentifier.get(),
				NULL_VALUE, inQuotes(modifier), VALUE_NAME_MODIFIER);
	}

	@Override
	public String formatInstructionsPerTestcase(MethodIdentifier testcaseIdentifier, int instructionCount) {
		return String.format(SQL_INSERT_TESTCASE_INFO_IMPORT, getExecutionId().getShortId(), testcaseIdentifier.get(),
				inQuotes(instructionCount), NULL_VALUE, VALUE_NAME_INSTRUCTIONS);
	}

	@Override
	public String formatAssertionsPerTestcase(MethodIdentifier testcaseIdentifier, int assertionCount) {
		return String.format(SQL_INSERT_TESTCASE_INFO_IMPORT, getExecutionId().getShortId(), testcaseIdentifier.get(),
				inQuotes(assertionCount), NULL_VALUE, VALUE_NAME_ASSERTIONS);
	}

	@Override
	public String formatCoveragePerMethod(MethodIdentifier methodIdentifier, ECoverageLevel coverageLevel,
			int coverageValue, ECoverageValueType valueType) {
		String valueName = getCoverageValueName(coverageLevel, valueType);

		return String.format(SQL_INSERT_METHOD_INFO_IMPORT, getExecutionId().getShortId(), methodIdentifier.get(),
				NULL_VALUE, inQuotes(coverageValue), valueName);
	}

	private String getCoverageValueName(ECoverageLevel coverageLevel, ECoverageValueType valueType) {
		return coverageLevel.getValueName() + valueType.getPostFix();
	}

	private String inQuotes(Object value) {
		return "'" + value + "'";
	}
}
