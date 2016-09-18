package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;

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
