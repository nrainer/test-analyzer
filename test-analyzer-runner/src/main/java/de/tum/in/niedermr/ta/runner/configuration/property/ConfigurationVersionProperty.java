package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractIntegerProperty;

public class ConfigurationVersionProperty extends AbstractIntegerProperty {
	public static final String NAME = "configurationVersion";
	private final int m_configurationVersionOfProgram;

	public ConfigurationVersionProperty(int configurationVersionOfProgram) {
		this.m_configurationVersionOfProgram = configurationVersionOfProgram;
	}

	@Override
	public final String getName() {
		return NAME;
	}

	@Override
	protected Integer getDefault() {
		return m_configurationVersionOfProgram;
	}

	public void setConfigurationVersionOfProgram() {
		setValue(m_configurationVersionOfProgram);
	}

	@Override
	public String getDescription() {
		return "Configuration version";
	}

	@Override
	public void validate() throws ConfigurationException {
		if (getValue() > m_configurationVersionOfProgram) {
			throw new ConfigurationException(this, "Illegal version");
		}
	}
}