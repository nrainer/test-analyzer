package de.tum.in.niedermr.ta.runner.configuration.exceptions;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Configuration exception. */
public class ConfigurationException extends Exception {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Constructor. */
	public ConfigurationException(Throwable t) {
		super(t);
	}

	/** Constructor. */
	public ConfigurationException(String msg) {
		super(msg);
	}

	/** Constructor. */
	public ConfigurationException(IConfigurationProperty<?> property, String msg) {
		this("At '" + property.getName() + "': ", msg);
	}

	/** Constructor. */
	private ConfigurationException(String prefix, String msg) {
		super(prefix + msg);
	}
}
