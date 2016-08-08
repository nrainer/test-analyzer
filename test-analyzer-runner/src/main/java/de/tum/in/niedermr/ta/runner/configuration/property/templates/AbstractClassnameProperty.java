package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public abstract class AbstractClassnameProperty<T> extends AbstractStringProperty {
	public final void setValue(Class<?> cls) {
		if (cls == null) {
			super.setValue(EMPTY_STRING);
		} else {
			super.setValue(cls.getName());
		}
	}

	public T createInstance() throws ReflectiveOperationException {
		if (isEmpty()) {
			return null;
		} else {
			String value = getValue();

			for (String constant : furtherAllowedConstants()) {
				if (value.equals(constant)) {
					return createInstanceFromConstant(value);
				}
			}

			return JavaUtility.createInstance(value);
		}
	}

	/**
	 * @param constant
	 */
	protected T createInstanceFromConstant(String constant) throws ReflectiveOperationException {
		return null;
	}

	protected String[] furtherAllowedConstants() {
		return new String[0];
	}

	/**
	 * @return class object of a class or an interface
	 */
	protected abstract Class<? extends T> getRequiredType();

	@Override
	protected final boolean isEmptyAllowed() {
		return false;
	}

	/**
	 * Valid if one of the following applies:
	 * <ul>
	 * <li>the value is an allowed constant</li>
	 * <li>a class with the value as name is on the classpath, the class can be instantiated and the class is an
	 * instance of the desired type</li>
	 * </ul>
	 */
	@Override
	protected final void validateFurther() throws ConfigurationException {
		if (isAllowedConstant()) {
			return;
		} else {
			validateClassName();
		}
	}

	private boolean isAllowedConstant() {
		final String value = getValue();

		if (furtherAllowedConstants() != null) {
			for (String s : furtherAllowedConstants()) {
				if (value.equals(s)) {
					return true;
				}
			}
		}

		return false;
	}

	/** @see #validateClassName(String, Class, IConfigurationProperty) */
	private void validateClassName() throws ConfigurationException {
		validateClassName(getValue(), getRequiredType(), this);
	}

	/** Validate that a class can be loaded, accessed and instantiated and be casted to a certain type. */
	public static void validateClassName(String className, Class<?> requiredType,
			IConfigurationProperty<?> propertyForExceptions) throws ConfigurationException {
		Class<?> cls;

		try {
			cls = Class.forName(className);
			cls.newInstance();
		} catch (ClassNotFoundException ex) {
			throw new ConfigurationException(propertyForExceptions, "Not in classpath: " + className + ".");
		} catch (NoClassDefFoundError ex) {
			throw new ConfigurationException(propertyForExceptions,
					"Referenced class not in classpath: " + className + ". (" + ex.getMessage() + ")");
		} catch (InstantiationException e) {
			throw new ConfigurationException(propertyForExceptions,
					"No parameterless constructor exists for " + className + ".");
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(propertyForExceptions, "Illegal access exception.");
		}

		if (!requiredType.isAssignableFrom(cls)) {
			throw new ConfigurationException(propertyForExceptions, "Invalid type: " + cls.getName());
		}
	}
}
