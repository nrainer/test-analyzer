package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

/** Abstract return value generator. */
public abstract class AbstractReturnValueGenerator implements IReturnValueGenerator {

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return getClass().getName();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getName();
	}
}
