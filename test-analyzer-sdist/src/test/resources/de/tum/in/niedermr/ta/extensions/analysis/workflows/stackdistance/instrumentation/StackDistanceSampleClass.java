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

	public int throwExternallyCreatedException() {
		throw createException();
	}

	private IllegalStateException createException() {
		return new IllegalStateException();
	}

	public int returnValue() {
		return 3;
	}

	public int multiReturnExits(Integer x) {
		if (x == null) {
			return 1;
		} else if (x > 3) {
			if (x > 100) {
				return 0;
			}

			return 4;
		}

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

	public int failIfTrue(Boolean fail) {
		if (fail) {
			throw new IllegalStateException("Fail requested");
		}

		return 0;
	}
}
