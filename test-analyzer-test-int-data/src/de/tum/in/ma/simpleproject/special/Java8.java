package de.tum.in.ma.simpleproject.special;

import java.util.function.Function;
import java.util.function.Predicate;

public class Java8 {
	public int applyMultiplyWithEight(int value) {
		return multiApplyFunction(x -> x + x, value, 3);
	}

	public int multiApplyFunction(Function<Integer, Integer> function, int value, int count) {
		int result = value;

		for (int i = 0; i < count; i++) {
			result = function.apply(result);
		}

		return result;
	}

	public int usePredicateLambda() {
		int result = apply(x -> (x > 0), 5);
		return result + 1;
	}

	protected int apply(Predicate<Integer> predicate, int value) {
		if (predicate.test(value)) {
			return value;
		}

		return 0;
	}
}
