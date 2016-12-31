package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import java.util.NoSuchElementException;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public abstract class AbstractReturnValueFactory implements IReturnValueFactory {

	/** {@inheritDoc} */
	@Override
	public boolean supports(MethodIdentifier methodIdentifier, String returnType) {
		try {
			getWithException(methodIdentifier, returnType);
			return true;
		} catch (NoSuchElementException ex) {
			return false;
		} catch (Throwable t) {
			return false;
		}
	}

	/** {@inheritDoc} */
	@Override
	public final Object get(String identifierAsString, String returnType) {
		try {
			return getWithException(MethodIdentifier.parse(identifierAsString), returnType);
		} catch (NoSuchElementException ex) {
			return null;
		}
	}
}
