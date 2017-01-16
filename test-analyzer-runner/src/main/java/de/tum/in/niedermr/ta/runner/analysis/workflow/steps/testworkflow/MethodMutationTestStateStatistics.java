package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import java.util.EnumMap;
import java.util.Map.Entry;

/** Statistics about the processing of methods. */
public class MethodMutationTestStateStatistics {

	/** Method count. */
	private int m_methodCount = 0;

	/** Contains for each method state how many methods belong to it. */
	private EnumMap<MethodMutationTestState, Integer> m_methodStateCount;

	/** Constructor. */
	public MethodMutationTestStateStatistics() {
		m_methodStateCount = new EnumMap<>(MethodMutationTestState.class);
	}

	/**
	 * Add a method by adding its mutation state. <br/>
	 * Note that a <b>method is supposed to be added with exactly one state.<b/>
	 */
	public void addMethod(MethodMutationTestState state) {
		addMethods(state, 1);
	}

	/** Add methods in the same state. */
	private void addMethods(MethodMutationTestState state, int occurrences) {
		m_methodStateCount.put(state, occurrences + getMethodCount(state));
		m_methodCount += occurrences;
	}

	/** Get the number of methods. */
	public int getMethodCount() {
		return m_methodCount;
	}

	/** Get the number of methods in the specified state. */
	public int getMethodCount(MethodMutationTestState state) {
		return m_methodStateCount.getOrDefault(state, 0);
	}

	/** Add values from another instance. */
	public void mergeWith(MethodMutationTestStateStatistics statistics) {
		for (Entry<MethodMutationTestState, Integer> otherEntry : statistics.m_methodStateCount.entrySet()) {
			if (otherEntry.getValue() != null) {
				addMethods(otherEntry.getKey(), otherEntry.getValue());
			}
		}
	}

	/** Get the summary. */
	public String toSummary() {
		return m_methodCount + " methods. " + getMethodCount(MethodMutationTestState.MUTATED_AND_TESTED)
				+ " processed successfully. " + getMethodCount(MethodMutationTestState.NOT_MUTATED) + " skipped. "
				+ getMethodCount(MethodMutationTestState.MUTATED_AND_TEST_TIMEOUT) + " with timeout. "
				+ getMethodCount(MethodMutationTestState.MUTATION_OR_TEST_FAILED) + " failed.";
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "MethodProcessingStatistics [" + toSummary() + "]";
	}
}
