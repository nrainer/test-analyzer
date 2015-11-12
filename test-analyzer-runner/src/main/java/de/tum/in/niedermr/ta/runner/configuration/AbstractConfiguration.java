package de.tum.in.niedermr.ta.runner.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.ConfigurationVersionProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

public abstract class AbstractConfiguration {

	private final ConfigurationVersionProperty m_configurationVersion;

	public AbstractConfiguration(int currentConfigurationVersion) {
		m_configurationVersion = new ConfigurationVersionProperty(currentConfigurationVersion);
	}

	/**
	 * [0] Configuration version.
	 */
	public ConfigurationVersionProperty getConfigurationVersion() {
		return m_configurationVersion;
	}

	public abstract List<IConfigurationProperty<?>> getAllPropertiesOrdered();

	public final void validate() throws ConfigurationException {
		Set<String> usedNames = new HashSet<>();

		for (IConfigurationProperty<?> property : getAllPropertiesOrdered()) {
			if (usedNames.contains(property.getName())) {
				throw new ConfigurationException("Name is already in use: " + property.getName());
			}

			property.validate();
			usedNames.add(property.getName());
		}
	}

	@Override
	public final String toString() {
		return Arrays.asList(getAllPropertiesOrdered()).toString();
	}

	public final String toMultiLineString() {
		StringBuilder result = new StringBuilder();

		for (IConfigurationProperty<?> property : getAllPropertiesOrdered()) {
			if (result.length() > 0) {
				result.append(CommonConstants.NEW_LINE);
			}

			result.append(property.toString());
		}

		return result.toString();
	}
}
