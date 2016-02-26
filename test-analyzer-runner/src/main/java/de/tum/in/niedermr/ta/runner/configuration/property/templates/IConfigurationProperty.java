package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public interface IConfigurationProperty<T> {

	String getName();

	T getValue();

	void setValue(T value);

	void setValueUnsafe(String stringValue) throws ConfigurationException;

	String getValueAsString();

	void setDefault();

	String getDescription();

	void validate() throws ConfigurationException;
}