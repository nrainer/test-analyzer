package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractSimpleAndWrapperReturnValueGenerator;

/** Simple return value generator that also supports wrapper types. */
public class SimpleAndWrapperReturnValueGeneratorWith1 extends AbstractSimpleAndWrapperReturnValueGenerator {

	/** Constructor. */
	public SimpleAndWrapperReturnValueGeneratorWith1() {
		super(new SimpleReturnValueGeneratorWith1());
	}
}
