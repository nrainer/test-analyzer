package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.result;

import java.util.Optional;

import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.PitOutputConverterWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.sql.SqlMultiInsertStatementBuilder;

/** Output builder for {@link PitOutputConverterWorkflow}. */
public class MutationSqlOutputBuilder {

	/** HTML encoded name of the constructor. */
	private static final String CONSTRUCTOR_ENCODED_METHOD_NAME = "&#60;init&#62;";

	/** SQL insert statement. */
	private static final String SQL_INSERT_STATEMENT = "INSERT INTO Pit_Mutation_Result_Import "
			+ "(execution, mutationIndex, mutatedMethod, mutationStatus, %s, %s, mutatorName, lineNumber, mutationDescription) "
			+ "VALUES %s;";

	/** Execution id. */
	private final IExecutionId m_executionId;

	/** Name of the test case identifier column in the insert statement. */
	private final String m_testcaseIdentifierColumnName;
	/** Name of the original test case name column in the insert statement. */
	private final String m_testcaseOrigColumnName;

	private boolean m_isIgnored = false;

	/** Mutation index. */
	private int m_mutationIndex;
	/** Line number. */
	private int m_lineNumber;

	/** Mutation status. */
	private String m_mutationStatus;
	/** Mutated method. */
	private MethodIdentifier m_mutatedMethod;
	/** Name of the mutator. */
	private String m_mutatorName;
	/** Identifier of the test case. */
	private Optional<TestcaseIdentifier> m_testcase;
	/** Signature (as specified in the XML file) of the test case. */
	private Optional<String> m_testcaseOrigSignature;
	/** Description of the mutation. */
	private String m_mutationDescription;

	/** Constructor. */
	public MutationSqlOutputBuilder(IExecutionId executionId, String testcaseIdentifierColumnName,
			String testcaseOrigColumnName) {
		m_executionId = executionId;
		m_testcaseIdentifierColumnName = testcaseIdentifierColumnName;
		m_testcaseOrigColumnName = testcaseOrigColumnName;
	}

	/** {@link m_mutatedMethod} */
	public MethodIdentifier getMutatedMethod() {
		return m_mutatedMethod;
	}

	/** {@link m_mutationStatus} */
	public String getMutationStatus() {
		return m_mutationStatus;
	}

	/** {@link m_mutationStatus} */
	public void setMutationStatus(String mutationStatus) {
		m_mutationStatus = mutationStatus;
	}

	/** {@link m_mutatedMethod} */
	public void setMutatedMethod(String mutatedClassName, String mutatedMethodName, String mutatedMethodTypeSignature) {
		if (CONSTRUCTOR_ENCODED_METHOD_NAME.equals(mutatedMethodName)) {
			mutatedMethodName = BytecodeConstants.NAME_CONSTRUCTOR;
		}

		m_mutatedMethod = MethodIdentifier.create(mutatedClassName, mutatedMethodName, mutatedMethodTypeSignature);
	}

	/** {@link m_mutatorName} */
	public void setMutatorName(String mutatorName) {
		m_mutatorName = mutatorName;
	}

	/** Set the signature of the test case. */
	public void setTestSignature(String testSignature) {
		if (StringUtility.isNullOrEmpty(testSignature)) {
			m_testcase = Optional.empty();
			m_testcaseOrigSignature = Optional.empty();
		} else {
			m_testcase = Optional.of(TestcaseIdentifier.createFromJavaName(testSignature));
			m_testcaseOrigSignature = Optional.of(testSignature);
		}
	}

	/** {@link m_mutatorDescription} */
	public void setMutationDescription(String mutationDescription) {
		m_mutationDescription = mutationDescription.replace("'", "");
	}

	/** {@link m_mutationIndex} */
	public void setMutationIndex(int index) {
		m_mutationIndex = index;
	}

	/** {@link m_lineNumber} */
	public void setLineNumber(int lineNumber) {
		m_lineNumber = lineNumber;
	}

	/** To SQL statement. */
	public String toSqlStatement() {
		if (isIgnored()) {
			return "";
		}

		return String.format(SQL_INSERT_STATEMENT, m_testcaseIdentifierColumnName, m_testcaseOrigColumnName,
				toValuesSqlStatementPart());
	}

	public SqlMultiInsertStatementBuilder createMultiInsertStatementBuilder() {
		return new SqlMultiInsertStatementBuilder(
				String.format(SQL_INSERT_STATEMENT, m_testcaseIdentifierColumnName, m_testcaseOrigColumnName, "%s"));
	}

	public void addToMultiInsertBuilder(SqlMultiInsertStatementBuilder builder) {
		if (isIgnored()) {
			return;
		}

		builder.addValuesStatementPart(toValuesSqlStatementPart());
	}

	private String toValuesSqlStatementPart() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(asSqlString(m_executionId.getShortId()));
		builder.append(", ");
		builder.append(m_mutationIndex);
		builder.append(", ");
		builder.append(asSqlString(m_mutatedMethod.get()));
		builder.append(", ");
		builder.append(asSqlString(m_mutationStatus));
		builder.append(", ");
		builder.append(m_testcase.map(identifier -> asSqlString(identifier.get())).orElse("NULL"));
		builder.append(", ");
		builder.append(m_testcaseOrigSignature.map(methodSignature -> asSqlString(methodSignature)).orElse("NULL"));
		builder.append(", ");
		builder.append(asSqlString(m_mutatorName));
		builder.append(", ");
		builder.append(m_lineNumber);
		builder.append(", ");
		builder.append(asSqlString(m_mutationDescription));
		builder.append(")");
		return builder.toString();
	}

	/** Wrap a string value in quotation marks. */
	private static String asSqlString(String value) {
		return SqlMultiInsertStatementBuilder.asSqlString(value);
	}

	public void ignoreNode() {
		m_isIgnored = true;
	}

	/** The content of this instance should not be included in the results. */
	public boolean isIgnored() {
		return m_isIgnored;
	}
}
