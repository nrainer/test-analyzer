package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.result;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link MutationSqlOutputBuilder}. */
public class MutationSqlOutputBuilderTest {

	/** Test. */
	@Test
	public void test() {
		MutationSqlOutputBuilder builder = new MutationSqlOutputBuilder(ExecutionIdFactory.ID_FOR_TESTS, "testcase",
				"testcaseOrig");
		builder.setMutatedMethod("java.lang.StringBuilder", "toString", "()Ljava/lang/String;");
		builder.setTestSignature("java.lang.StringBuilderTest.testToString(java.lang.StringBuilderTest)");
		builder.setMutatorName("org.pitest.experimental.LogicRemover");
		builder.setMutationDescription("Remove logic");
		builder.setMutationStatus("KILLED");

		String expected = "INSERT INTO Pit_Mutation_Result_Import"
				+ " (execution, mutatedMethod, mutationStatus, testcase, testcaseOrig, mutatorName, mutationDescription) VALUES"
				+ " ('TEST', 'java.lang.StringBuilder.toString()', 'KILLED', 'java.lang.StringBuilderTest;testToString',"
				+ " 'java.lang.StringBuilderTest.testToString(java.lang.StringBuilderTest)', 'org.pitest.experimental.LogicRemover',"
				+ " 'Remove logic');";
		assertEquals(expected, builder.complete());
	}
}
