package de.tum.in.niedermr.ta.runner.configuration.exceptions;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

public class ConfigurationException extends Exception {
	private static final long serialVersionUID = 7514093845938362369L;

	public ConfigurationException(Throwable t) {
		super(t);
	}

	public ConfigurationException(String msg) {
		this("", msg);
	}

	public ConfigurationException(IConfigurationProperty<?> property, String msg) {
		this("At '" + property.getName() + "': ", msg);
	}

	private ConfigurationException(String prefix, String msg) {
		super(prefix + msg);
	}
}
