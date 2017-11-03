package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

public class StackDistanceSampleClass {

	public void empty() {
		// NOP
	}

	public int returnMethodResult() {
		return returnValue();
	}

	public int throwException() {
		throw new IllegalStateException();
	}

	public int returnValue() {
		return 3;
	}

	public int computation() {
		int x = 3;
		return x << 1;
	}

	public int failInputDependent(Boolean fail) {
		int x = 4;
		return x + failIfTrue(fail);
	}

	@SuppressWarnings("finally")
	public int tryFinally() {
		try {
			return 3;
		} finally {
			return -1;
		}
	}

	private int failIfTrue(boolean fail) {
		if (fail) {
			throw new IllegalStateException("Fail requested");
		}

		return 0;
	}
}
