package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.parser.IConfigurationTokens;

public abstract class AbstractConfigurationProperty<T> implements IConfigurationProperty<T> {
	public static final String PLACEHOLDER_DEFAULT = "@DEFAULT";

	private T m_value;

	public AbstractConfigurationProperty() {
		setDefault();
	}

	/** {@inheritDoc} */
	@Override
	public abstract String getName();

	/** {@inheritDoc} */
	@Override
	public final T getValue() {
		return m_value;
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(T value) {
		this.m_value = value;
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public final String getValueAsString() {
		if (m_value == null) {
			return "";
		}

		return m_value.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void setDefault() {
		setValue(getDefault());
	}

	/** {@inheritDoc} */
	@Override
	public abstract String getDescription();

	protected abstract T getDefault();

	/** {@inheritDoc} */
	@Override
	public void validate() throws ConfigurationException {
		// NOP
	}

	/**
	 * @param valueToParse
	 * @return
	 */
	protected abstract T parseValue(String valueToParse);

	/** {@inheritDoc} */
	@Override
	public final String toString() {
		return toString(getName(), getValueAsString(), false);
	}

	/** {@inheritDoc} */
	@Override
	public final String toParseableString() {
		return toString(getName(), getValueAsString(), true);
	}

	public static String toString(String name, Object value, boolean parseable) {
		String line = name + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + value;

		if (parseable) {
			return line;
		}

		return "[" + line + "]";
	}
}
