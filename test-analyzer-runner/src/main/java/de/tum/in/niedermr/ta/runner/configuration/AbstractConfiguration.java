package de.tum.in.niedermr.ta.runner.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationValuesManager;
import de.tum.in.niedermr.ta.runner.configuration.property.ConfigurationVersionProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/**
 * Configuration base. <br/>
 * A configuration consists of built-in {@link IConfigurationProperty}s and of (untyped) dynamic configuration
 * properties managed by {@link DynamicConfigurationValuesManager}. The dynamic properties are accessed by their
 * {@link DynamicConfigurationKey} and are used for advanced settings or settings of extensions.
 */
public abstract class AbstractConfiguration {

	private final ConfigurationVersionProperty m_configurationVersion;
	private final DynamicConfigurationValuesManager my_dynamicValuesManager;

	public AbstractConfiguration(int currentConfigurationVersion) {
		m_configurationVersion = new ConfigurationVersionProperty(currentConfigurationVersion);
		my_dynamicValuesManager = new DynamicConfigurationValuesManager();
	}

	/**
	 * [0] Configuration version.
	 */
	public ConfigurationVersionProperty getConfigurationVersion() {
		return m_configurationVersion;
	}

	/** Access the dynamic property values. */
	public DynamicConfigurationValuesManager getDynamicValues() {
		return my_dynamicValuesManager;
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
		return "[" + Arrays.asList(getAllPropertiesOrdered()).toString() + ", " + my_dynamicValuesManager.toString()
				+ "]";
	}

	public final String toMultiLineString() {
		StringBuilder result = new StringBuilder();

		List<String> configurationLines = new ArrayList<>();
		configurationLines.addAll(builtInPropertiesToStringLines());
		configurationLines.addAll(my_dynamicValuesManager.toStringLines());

		for (String line : configurationLines) {
			if (result.length() > 0) {
				result.append(CommonConstants.NEW_LINE);
			}

			result.append(line);
		}

		return result.toString();
	}

	private List<String> builtInPropertiesToStringLines() {
		List<String> list = new ArrayList<>();

		for (IConfigurationProperty<?> property : getAllPropertiesOrdered()) {
			list.add(property.toString());
		}

		return list;
	}
}
