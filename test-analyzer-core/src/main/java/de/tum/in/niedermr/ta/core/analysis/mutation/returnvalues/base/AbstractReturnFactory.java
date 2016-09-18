package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import java.util.NoSuchElementException;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public abstract class AbstractReturnFactory {
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

	public final Object get(String identifierAsString, String returnType) {
		try {
			return getWithException(MethodIdentifier.parse(identifierAsString), returnType);
		} catch (NoSuchElementException ex) {
			return null;
		}
	}

	public abstract Object getWithException(MethodIdentifier methodIdentifier, String returnType) throws NoSuchElementException;
}
