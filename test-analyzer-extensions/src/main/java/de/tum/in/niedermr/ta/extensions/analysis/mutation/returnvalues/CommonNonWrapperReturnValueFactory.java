package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import java.util.NoSuchElementException;

public class CommonNonWrapperReturnValueFactory extends CommonReturnValueFactory {

	@SuppressWarnings("hiding")
	public static final CommonNonWrapperReturnValueFactory INSTANCE = new CommonNonWrapperReturnValueFactory();

	/** {@inheritDoc} */
	@Override
	protected Object tryCreateJavaLangWrapper(String returnType) throws NoSuchElementException {
		throw new NoSuchElementException();
	}
}
