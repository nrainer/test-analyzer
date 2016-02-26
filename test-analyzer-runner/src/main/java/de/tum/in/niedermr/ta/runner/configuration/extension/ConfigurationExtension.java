package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractConfigurationProperty;

public class ConfigurationExtension {

	private final Map<ConfigurationExtensionKey, String> m_dataMap;

	public ConfigurationExtension() {
		m_dataMap = new HashMap<>();
	}

	public void setRawValue(ConfigurationExtensionKey key, String value) {
		m_dataMap.put(key, value);
	}

	public boolean isSet(ConfigurationExtensionKey key) {
		return m_dataMap.containsKey(key);
	}

	public String getStringValue(ConfigurationExtensionKey key, String valueIfNotSet) {
		return m_dataMap.getOrDefault(key, valueIfNotSet);
	}

	public Integer getIntValue(ConfigurationExtensionKey key, Integer valueIfNotSet) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return valueIfNotSet;
		}

		return Integer.parseInt(stringValue);
	}

	public boolean getBooleanValue(ConfigurationExtensionKey key, boolean valueIfNotSet) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return valueIfNotSet;
		}

		return Boolean.parseBoolean(stringValue);
	}

	public boolean getBooleanValue(ConfigurationExtensionKey key) {
		return getBooleanValue(key, false);
	}

	@Override
	public final String toString() {
		return toStringLines().toString();
	}

	private String toString(Entry<ConfigurationExtensionKey, String> entry) {
		String propertyName = entry.getKey().getName();
		return AbstractConfigurationProperty.toString(propertyName, entry.getValue());
	}

	public List<String> toStringLines() {
		List<String> list = new ArrayList<>();

		for (Entry<ConfigurationExtensionKey, String> entry : m_dataMap.entrySet()) {
			list.add(toString(entry));
		}

		return list;
	}
}
