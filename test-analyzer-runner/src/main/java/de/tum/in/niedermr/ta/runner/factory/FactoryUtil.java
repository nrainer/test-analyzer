package de.tum.in.niedermr.ta.runner.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
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
	public static IFactory tryCreateFactoryOrUseDefault(Configuration configuration, ClassLoader classLoader)
			throws ReflectiveOperationException {
		String factoryClassName = configuration.getFactoryClass().getValue();

		if (!JavaUtility.isClassAvailable(factoryClassName, classLoader)) {
			LOGGER.info(factoryClassName + " is not yet on the classpath. Using " + DefaultFactory.class.getName()
					+ " in this process.");
			return new DefaultFactory();
		}

		return configuration.getFactoryClass().createInstance(classLoader);
	}

	/** Create an instance of the factory. */
	public static IFactory createFactory(Configuration configuration, ClassLoader classLoader)
			throws ExecutionException {
		try {
			return configuration.getFactoryClass().createInstance(classLoader);
		} catch (ReflectiveOperationException e) {
			throw new ExecutionException("Factory creation failed", e);
		}
	}
}
