package de.tum.in.niedermr.ta.extensions.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.result.MutationSqlOutputBuilder;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageLevel;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageValueType;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.result.AggregatedCoverageSqlOutputBuilder;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.DatabaseResultPresentation;

/** An extended version of the database result presentation. */
public class ExtendedDatabaseResultPresentation extends DatabaseResultPresentation
		implements IResultPresentationExtended {

	private static final String SQL_INSERT_STACK_INFO_IMPORT = "INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance, invocationCount) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');";
	private static final String SQL_INSERT_METHOD_INFO_IMPORT = "INSERT INTO Method_Info_Import (execution, method, %s, valueName) VALUES ('%s', '%s', '%s', '%s');";
	private static final String SQL_INSERT_TESTCASE_INFO_IMPORT = "INSERT INTO Testcase_Info_Import (execution, testcase, %s, valueName) VALUES ('%s', '%s', '%s', '%s');";

	private static final String VALUE_NAME_INSTRUCTIONS = "instructions";
	private static final String VALUE_NAME_MODIFIER = "modifier";
	private static final String VALUE_NAME_ASSERTIONS = "assertions";

	/** {@inheritDoc} */
	@Override
	public String formatStackDistanceInfoEntry(TestcaseIdentifier testcaseIdentifier, MethodIdentifier methodUnderTest,
			int minInvocationDistance, int maxInvocationDistance, int invocationCount) {
		return String.format(SQL_INSERT_STACK_INFO_IMPORT, getExecutionId().getShortId(),
				testcaseIdentifier.toMethodIdentifier().get(), methodUnderTest.get(), minInvocationDistance,
				maxInvocationDistance, invocationCount);
	}

	/** {@inheritDoc} */
	@Override
	public String formatInstructionsPerMethod(MethodIdentifier methodIdentifier, int instructionCount) {
		return formatInsertIntoMethodInfo(methodIdentifier, VALUE_NAME_INSTRUCTIONS, instructionCount, null);
	}

	/** {@inheritDoc} */
	@Override
	public String formatModifierPerMethod(MethodIdentifier methodIdentifier, String modifier) {
		return formatInsertIntoMethodInfo(methodIdentifier, VALUE_NAME_MODIFIER, null, modifier);
	}

	/** {@inheritDoc} */
	@Override
	public String formatInstructionsPerTestcase(TestcaseIdentifier testcaseIdentifier, int instructionCount) {
		return formatInsertIntoTestcaseInfo(testcaseIdentifier, VALUE_NAME_INSTRUCTIONS, instructionCount, null);
	}

	/** {@inheritDoc} */
	@Override
	public String formatAssertionsPerTestcase(TestcaseIdentifier testcaseIdentifier, int assertionCount) {
		return formatInsertIntoTestcaseInfo(testcaseIdentifier, VALUE_NAME_ASSERTIONS, assertionCount, null);
	}

	/** {@inheritDoc} */
	@Override
	public String formatCoveragePerMethod(MethodIdentifier methodIdentifier, ECoverageLevel coverageLevel,
			int coverageValue, ECoverageValueType valueType) {
		String valueName = getCoverageValueName(coverageLevel, valueType);
		return formatInsertIntoMethodInfo(methodIdentifier, valueName, coverageValue, null);
	}

	private String formatInsertIntoTestcaseInfo(TestcaseIdentifier testcaseIdentifier, String valueName,
			Integer intValue, String stringValue) {
		String valueColumnName = getValueColumnName(intValue, stringValue);
		Object valueColumnContent = getValueColumnContent(intValue, stringValue);

		return String.format(SQL_INSERT_TESTCASE_INFO_IMPORT, valueColumnName, getExecutionId().getShortId(),
				testcaseIdentifier.toMethodIdentifier().get(), valueColumnContent, valueName);
	}

	private String formatInsertIntoMethodInfo(MethodIdentifier methodIdentifier, String valueName, Integer intValue,
			String stringValue) {
		String valueColumnName = getValueColumnName(intValue, stringValue);
		Object valueColumnContent = getValueColumnContent(intValue, stringValue);

		return String.format(SQL_INSERT_METHOD_INFO_IMPORT, valueColumnName, getExecutionId().getShortId(),
				methodIdentifier.get(), valueColumnContent, valueName);
	}

	private Object getValueColumnContent(Integer intValue, String stringValue) {
		if (intValue != null) {
			return intValue;
		}

		return stringValue;
	}

	private String getValueColumnName(Integer intValue, String stringValue) {
		if (intValue != null && stringValue != null) {
			throw new IllegalArgumentException("Only intValue or stringValue can be used.");
		}

		if (intValue != null) {
			return "intValue";
		} else {
			return "stringValue";
		}
	}

	/** {@inheritDoc} */
	@Override
	public AggregatedCoverageSqlOutputBuilder createProjectCoverageSqlOutputBuilder(ECoverageLevel coverageLevel) {
		return new AggregatedCoverageSqlOutputBuilder(getExecutionId(), coverageLevel);
	}

	private String getCoverageValueName(ECoverageLevel coverageLevel, ECoverageValueType valueType) {
		return coverageLevel.getValueName() + valueType.getPostFix();
	}

	/** {@inheritDoc} */
	@Override
	public MutationSqlOutputBuilder createMutationSqlOutputBuilder(String testcaseIdentifierColumnName,
			String testcaseOrigColumnName) {
		return new MutationSqlOutputBuilder(getExecutionId(), testcaseIdentifierColumnName, testcaseOrigColumnName);
	}
}
