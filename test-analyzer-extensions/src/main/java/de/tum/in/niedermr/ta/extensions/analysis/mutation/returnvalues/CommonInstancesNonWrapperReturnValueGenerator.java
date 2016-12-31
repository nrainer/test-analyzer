package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import java.io.InvalidClassException;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractFactoryReturnValueGenerator;

/**
 * Return value generator which can handle method return types of often used instances (such as Date, List, Set).
 * Primitive return types, String and void are not supported.
 * 
 * @see CommonReturnValueFactory
 */
public class CommonInstancesNonWrapperReturnValueGenerator extends AbstractFactoryReturnValueGenerator {
	public CommonInstancesNonWrapperReturnValueGenerator() throws ReflectiveOperationException, InvalidClassException {
		super(CommonNonWrapperReturnValueFactory.class);
	}
}