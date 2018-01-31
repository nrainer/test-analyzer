package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

/** Coverage state of a source code line. */
public enum ELineCoverageState {

	/** Not coverable. */
	NOT_COVERABLE,
	/** Not covered. */
	NOT_COVERED,
	/** Partially covered. */
	PARTIALLY_COVERED,
	/** Fully covered. */
	FULLY_COVERED;

	/** Get the state based on instruction counts. */
	public static ELineCoverageState get(int countCoveredInstructions, int countMissedInstructions) {
		if (countCoveredInstructions < 0 || countMissedInstructions < 0) {
			throw new IllegalStateException("Counters must not be < 0");
		}

		if (countCoveredInstructions > 0 && countMissedInstructions == 0) {
			return FULLY_COVERED;
		}

		if (countCoveredInstructions == 0 && countMissedInstructions > 0) {
			return NOT_COVERED;
		}

		if (countCoveredInstructions > 0 && countMissedInstructions > 0) {
			return PARTIALLY_COVERED;
		}

		// both are zero
		return NOT_COVERABLE;
	}

	/** Get the name. */
	public String getName() {
		return toString();
	}
}
