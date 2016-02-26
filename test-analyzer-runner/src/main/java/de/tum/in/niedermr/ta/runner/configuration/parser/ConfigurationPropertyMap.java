package de.tum.in.niedermr.ta.runner.configuration.parser;

import java.util.HashMap;
import java.util.Map;

import de.tum.in.niedermr.ta.runner.configuration.AbstractConfiguration;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

class ConfigurationPropertyMap {
	private Map<String, IConfigurationProperty<?>> m_allProperties;

	public ConfigurationPropertyMap(AbstractConfiguration configuration) {
		initPropertiesAsMap(configuration);
	}

	private void initPropertiesAsMap(AbstractConfiguration configuration) {
		this.m_allProperties = new HashMap<>();

		for (IConfigurationProperty<?> property : configuration.getAllPropertiesOrdered()) {
			m_allProperties.put(property.getName(), property);
		}
	}

	public IConfigurationProperty<?> getPropertyByKey(String key) {
		return m_allProperties.get(key);
	}
}
