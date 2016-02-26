package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public abstract class AbstractConfigurationProperty<T> implements IConfigurationProperty<T> {
	public static final String PLACEHOLDER_DEFAULT = "@DEFAULT";

	private T m_value;

	public AbstractConfigurationProperty() {
		setDefault();
	}

	@Override
	public abstract String getName();

	@Override
	public final T getValue() {
		return m_value;
	}

	@Override
	public void setValue(T value) {
		this.m_value = value;
	}

	@Override
	public final void setValueUnsafe(String stringValue) throws ConfigurationException {
		try {
			if (stringValue.equals(PLACEHOLDER_DEFAULT)) {
				setDefault();
			} else {
				setValue(parseValue(stringValue));
			}
		} catch (RuntimeException ex) {
			throw new ConfigurationException(this, "Error when setting value '" + stringValue + "'");
		}
	}

	@Override
	public final String getValueAsString() {
		return m_value == null ? "" : m_value.toString();
	}

	@Override
	public void setDefault() {
		setValue(getDefault());
	}

	@Override
	public abstract String getDescription();

	protected abstract T getDefault();

	@Override
	public void validate() throws ConfigurationException {
		// NOP
	}

	/**
	 * @param valueToParse
	 * @return
	 */
	protected abstract T parseValue(String valueToParse);

	@Override
	public final String toString() {
		return "[" + toString(getName(), getValue()) + "]";
	}

	public static String toString(String name, Object value) {
		return name + " = " + value;
	}
}
