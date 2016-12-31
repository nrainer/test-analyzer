package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import java.util.NoSuchElementException;
import java.util.Optional;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public abstract class AbstractReturnValueFactory implements IReturnValueFactory {

	private Optional<AbstractReturnValueFactory> m_fallbackFactory;

	public AbstractReturnValueFactory() {
		m_fallbackFactory = getConfiguredFallbackFactory();
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(MethodIdentifier methodIdentifier, String returnType) {
		try {
			getRecursiveWithException(methodIdentifier, returnType);
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
			return getRecursiveWithException(MethodIdentifier.parse(identifierAsString), returnType);
		} catch (NoSuchElementException ex) {
			return null;
		}
	}

	protected Object getRecursiveWithException(MethodIdentifier methodIdentifier, String returnType)
			throws NoSuchElementException {
		try {
			return getWithException(methodIdentifier, returnType);
		} catch (NoSuchElementException e) {
			if (m_fallbackFactory.isPresent()) {
				return m_fallbackFactory.get().getWithException(methodIdentifier, returnType);
			}

			throw e;
		}
	}

	/** Get a fallback factory in case this factory does not support the given type. */
	protected Optional<AbstractReturnValueFactory> getConfiguredFallbackFactory() {
		return Optional.empty();
	}

	protected abstract Object getWithException(MethodIdentifier methodIdentifier, String returnType)
			throws NoSuchElementException;
}
