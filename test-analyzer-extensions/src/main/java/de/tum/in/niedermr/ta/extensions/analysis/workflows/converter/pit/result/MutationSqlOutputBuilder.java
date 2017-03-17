package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.result;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.PitOutputConverterWorkflow;

/** Output builder for {@link PitOutputConverterWorkflow}. */
public class MutationSqlOutputBuilder {

	/** HTML encoded name of the constructor. */
	private static final String CONSTRUCTOR_ENCODED_METHOD_NAME = "&#60;init&#62;";
	/** Actual name of the constructor. */
	private static final String CONSTRUCTOR_METHOD_NAME = "<init>";

	/** SQL insert statement. */
	private static final String SQL_INSERT_STATEMENT = "INSERT INTO Pit_Mutation_Result "
			+ "(mutationStatus, mutatedClass, mutatedMethod, mutatedMethodTypeSignature, mutatorName, killingTestSignature, mutationDescription) "
			+ "VALUES (%s);";

	/** Mutation status. */
	private String m_mutationStatus;
	/** Mutated class. */
	private String m_mutatedClass;
	/** Mutated method. */
	private String m_mutatedMethod;
	/** Types of the parameters and return type of the mutated method (byte-code style). */
	private String m_mutatedMethodTypeSignature;
	/** Name of the mutator. */
	private String m_mutatorName;
	/** Name of the test case that first killed the method. */
	private String m_killingTestSignature;
	/** Description of the mutation. */
	private String m_mutationDescription;

	/** {@link m_mutationStatus} */
	public void setMutationStatus(String mutationStatus) {
		m_mutationStatus = mutationStatus;
	}

	/** {@link m_mutatedClass} */
	public void setMutatedClass(String mutatedClass) {
		m_mutatedClass = mutatedClass;
	}

	/** {@link m_mutatedMethod} */
	public void setMutatedMethod(String mutatedMethod) {
		if (CONSTRUCTOR_ENCODED_METHOD_NAME.equals(mutatedMethod)) {
			m_mutatedMethod = CONSTRUCTOR_METHOD_NAME;
		} else {
			m_mutatedMethod = mutatedMethod;
		}
	}

	/** {@link m_mutatedMethodTypeSignature} */
	public void setMutatedMethodTypeSignature(String mutatedMethodTypeSignature) {
		m_mutatedMethodTypeSignature = mutatedMethodTypeSignature;
	}

	/** {@link m_mutatorName} */
	public void setMutatorName(String mutatorName) {
		m_mutatorName = mutatorName;
	}

	/** {@link m_killingTestSignature} */
	public void setKillingTestSignature(String killingTestSignature) {
		m_killingTestSignature = killingTestSignature;
	}

	/** {@link m_mutatorDescription} */
	public void setMutationDescription(String mutationDescription) {
		m_mutationDescription = mutationDescription.replace("'", "");
	}

	/** Complete. */
	public String complete() {
		StringBuilder builder = new StringBuilder();
		builder.append(asSqlString(m_mutationStatus));
		builder.append(", ");
		builder.append(asSqlString(m_mutatedClass));
		builder.append(", ");
		builder.append(asSqlString(m_mutatedMethod));
		builder.append(", ");
		builder.append(asSqlString(m_mutatedMethodTypeSignature));
		builder.append(", ");
		builder.append(asSqlString(m_mutatorName));
		builder.append(", ");
		builder.append(asSqlString(m_killingTestSignature));
		builder.append(", ");
		builder.append(asSqlString(m_mutationDescription));

		return String.format(SQL_INSERT_STATEMENT, builder.toString());
	}

	/** Wrap a string value in quotation marks. */
	private String asSqlString(String value) {
		return "'" + value + "'";
	}
}
