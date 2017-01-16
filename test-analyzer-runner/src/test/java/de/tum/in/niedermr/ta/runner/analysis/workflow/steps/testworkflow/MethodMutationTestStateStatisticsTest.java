package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Test {@link MethodMutationTestStateStatistics}. */
public class MethodMutationTestStateStatisticsTest {

	/** Test. */
	@Test
	public void test() {
		MethodMutationTestStateStatistics statistics = new MethodMutationTestStateStatistics();
		statistics.addMethod(MethodMutationTestState.MUTATED_AND_TEST_TIMEOUT);
		statistics.addMethod(MethodMutationTestState.NOT_MUTATED);
		statistics.addMethod(MethodMutationTestState.MUTATED_AND_TESTED);
		statistics.addMethod(MethodMutationTestState.MUTATED_AND_TESTED);
		statistics.addMethod(MethodMutationTestState.MUTATED_AND_TESTED);

		assertEquals(5, statistics.getMethodCount());
		assertEquals(0, statistics.getMethodCount(MethodMutationTestState.MUTATION_OR_TEST_FAILED));
		assertEquals(1, statistics.getMethodCount(MethodMutationTestState.NOT_MUTATED));
		assertEquals(3, statistics.getMethodCount(MethodMutationTestState.MUTATED_AND_TESTED));

		assertEquals("5 methods. 3 processed successfully. 1 skipped. 1 with timeout. 0 failed.",
				statistics.toSummary());

		MethodMutationTestStateStatistics statistics2 = new MethodMutationTestStateStatistics();
		statistics.addMethod(MethodMutationTestState.NOT_MUTATED);

		statistics.mergeWith(statistics2);

		assertEquals(6, statistics.getMethodCount());
		assertEquals(2, statistics.getMethodCount(MethodMutationTestState.NOT_MUTATED));
		assertEquals(3, statistics.getMethodCount(MethodMutationTestState.MUTATED_AND_TESTED));
		assertEquals("6 methods. 3 processed successfully. 2 skipped. 1 with timeout. 0 failed.",
				statistics.toSummary());
	}
}
