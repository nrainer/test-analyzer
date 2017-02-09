package de.tum.in.niedermr.ta.runner.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Factory util. */
public final class FactoryUtil {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(FactoryUtil.class);

	/** Constructor. */
	private FactoryUtil() {
		// NOP
	}

	/**
	 * Try to create the specified factory. If this fails (because the specified factory is not yet on the classpath),
	 * the default factory will be created.
	 * 
	 * @throws ReflectiveOperationException
	 */
	public static IFactory tryCreateFactoryOrUseDefault(Configuration configuration)
			throws ReflectiveOperationException {
		String factoryClassName = configuration.getFactoryClass().getValue();

		try {
			Class.forName(factoryClassName);
		} catch (ClassNotFoundException e) {
			LOGGER.info(factoryClassName + " is not yet on the classpath. Using " + DefaultFactory.class.getName()
					+ " in this process.");
			return new DefaultFactory();
		}

		return configuration.getFactoryClass().createInstance();
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
