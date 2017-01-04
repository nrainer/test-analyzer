package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public abstract class AbstractReturnValueFactory implements IReturnValueFactory {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractReturnValueFactory.class);

	private Optional<AbstractReturnValueFactory> m_fallbackFactory;

	public AbstractReturnValueFactory() {
		m_fallbackFactory = getConfiguredFallbackFactory();
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(MethodIdentifier methodIdentifier, String returnType) {
		try {
			createRecursiveWithException(methodIdentifier, returnType);
			return true;
		} catch (NoSuchElementException ex) {
			return false;
		} catch (Throwable t) {
			return false;
		}
	}

	/**
	 * {@inheritDoc} <b>DO NOT CHANGE THE METHOD SIGNATURE: Method is invoked from mutated code.</b>
	 */
	@Override
	public final Object get(String identifierAsString, String returnType) {
		try {
			return createRecursiveWithException(MethodIdentifier.parse(identifierAsString), returnType);
		} catch (Throwable t) {
			// should not happen because this method gets only invoked if the supports method returns true
			LOGGER.error("Return null because instance creation failed unexpectedly", t);
			return null;
		}
	}

	/** Create an instance using {@link #createWithException(MethodIdentifier, String)} or use the fallback factory. */
	protected final Object createRecursiveWithException(MethodIdentifier methodIdentifier, String returnType)
			throws Throwable, NoSuchElementException {
		try {
			return createWithException(methodIdentifier, returnType);
		} catch (Throwable t) {
			if (m_fallbackFactory.isPresent()) {
				return m_fallbackFactory.get().createRecursiveWithException(methodIdentifier, returnType);
			}

			throw t;
		}
	}

	/** Create an instance or throw a NoSuchElementException if the class is not supported. */
	protected abstract Object createWithException(MethodIdentifier methodIdentifier, String returnType)
			throws Throwable, NoSuchElementException;

	/** Get a fallback factory in case this factory does not support the given type. */
	protected Optional<AbstractReturnValueFactory> getConfiguredFallbackFactory() {
		return Optional.empty();
	}
}
