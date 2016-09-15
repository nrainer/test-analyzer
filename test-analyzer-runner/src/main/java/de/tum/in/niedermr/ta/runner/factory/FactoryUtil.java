package de.tum.in.niedermr.ta.runner.factory;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Factory util. */
public final class FactoryUtil {

	/** Constructor. */
	private FactoryUtil() {
		// NOP
	}

	/** Create the default factory. */
	public static IFactory createDefaultFactory() {
		return new DefaultFactory();
	}

	/** Create an instance of the factory. */
	public static IFactory createFactory(Configuration configuration) throws ExecutionException {
		try {
			return configuration.getFactoryClass().createInstance();
		} catch (ReflectiveOperationException e) {
			throw new ExecutionException("Factory creation failed", e);
		}
	}
}
