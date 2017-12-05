package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

public class StackDistanceSampleClass {

	public void empty() {
		// NOP
	}

	public int returnMethodResult() {
		return returnValue();
	}

	public int delegateToFailIfTrue() {
		failIfTrue(true);
		return 0;
	}

	public int delegateToDelegateToFailIfTrue() {
		try {
			delegateToDelegateToFailIfTrue();
		} catch (Exception e) {
			// NOP
		}

		recursive(5);

		return 0;
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

	public int multiExits(Integer x) {
		if (x == null) {
			return 1;
		} else if (x < 1000) {
			if (x == 100) {
				return 0;
			} else if (x == 80) {
				failIfTrue(true);
			}

			throw new IllegalArgumentException();
		}

		return 3;
	}

	public synchronized int synchronizedMultiExits(Integer x) {
		if (x == null) {
			return 1;
		} else if (x < 1000) {
			if (x == 100) {
				return 0;
			} else if (x == 80) {
				failIfTrue(true);
			}

			throw new IllegalArgumentException();
		}

		return 3;
	}

	public int computation() {
		int x = 3;
		return x << 1;
	}

	public void recursive(Integer n) {
		if (n <= 1) {
			return;
		}

		recursive(n - 1);
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

	public int tryCatch(Integer x) {
		try {
			if (x == 1) {
				throw new Exception();
			}

			if (x == 2) {
				return 2;
			}
		} catch (Exception e) {
			return 3;
		}

		return 4;
	}

	public int throwCatchThrow() throws Exception {
		try {
			throw new Exception();
		} catch (Exception e) {
			throw e;
		}
	}

	public int synchronizedThrowCatchThrow1() throws Exception {
		try {
			throw new Exception();
		} catch (Exception e) {
			synchronized (this) {
				throw e;
			}
		}
	}

	public int synchronizedThrowCatchThrow2() throws Exception {
		synchronized (this) {
			try {
				throw new Exception();
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public int synchronizedBlock() {
		int x = returnValue();

		synchronized (this) {

			if (x > 3) {
				return x;
			}

			x++;
		}

		return x;
	}

	public int failIfTrue(Boolean fail) {
		if (fail) {
			throw new IllegalStateException("Fail requested");
		}

		return 0;
	}

	public int failIfFalse(Boolean failNot) {
		if (failNot) {
			return 0;
		}

		throw new IllegalStateException("Fail requested");
	}
}
