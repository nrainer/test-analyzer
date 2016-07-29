package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source;

public class ClassToBeInstrumented {
	public int compute(int value) {
		if (value < 0) {
			return handleNegativeValue();
		} else if (value == 0) {
			return 0;
		}

		return computeInternal(value);
	}

	private int computeInternal(int value) {
		return increase(value) + 5;
	}

	private int increase(int value) {
		return value + 7;
	}

	private int handleNegativeValue() {
		throw new IllegalArgumentException();
	}
}