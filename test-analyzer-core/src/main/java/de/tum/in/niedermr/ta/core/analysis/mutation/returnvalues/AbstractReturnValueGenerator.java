package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

public abstract class AbstractReturnValueGenerator implements IReturnValueGenerator {

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public String toString() {
		return getName();
	}
}
