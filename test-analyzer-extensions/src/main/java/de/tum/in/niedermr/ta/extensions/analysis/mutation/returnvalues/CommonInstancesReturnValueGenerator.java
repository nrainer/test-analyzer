package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.AbstractFactoryReturnValueGenerator;

/**
 * Return value generator which can handle method return types of often used instances (such as Date, List, Set). Primitive return types, String and void are
 * not supported.
 * 
 * @see CommonFactory
 */
public class CommonInstancesReturnValueGenerator extends AbstractFactoryReturnValueGenerator {
	public CommonInstancesReturnValueGenerator() throws Exception {
		super(CommonFactory.class);
	}
}