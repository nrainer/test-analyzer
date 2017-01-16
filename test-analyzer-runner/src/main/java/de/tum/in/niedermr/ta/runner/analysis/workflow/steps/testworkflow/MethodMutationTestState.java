package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

/** State of a mutated method. */
public enum MethodMutationTestState {

	/** Method was mutated and tested. */
	MUTATED_AND_TESTED,

	/** Method was mutated and the test resulted in a timeout. */
	MUTATED_AND_TEST_TIMEOUT,

	/** Mutation or test of the method failed. */
	MUTATION_OR_TEST_FAILED,

	/**
	 * Method was skipped: Mutation was not supported (because of the return value generator) or method is filtered.
	 */
	NOT_MUTATED,

	/** Unknown. */
	UNKNOWN;

	/** Return the state that is most thorough. */
	public static MethodMutationTestState reduceToBestState(MethodMutationTestState state1,
			MethodMutationTestState state2) {
		if (state1.ordinal() < state2.ordinal()) {
			return state1;
		}

		return state2;
	}
}
