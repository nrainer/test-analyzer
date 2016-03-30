package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

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

	public String getStringValue(ConfigurationExtensionKey key) {
		return getStringValue(key, null);
	}

	public String getStringValue(ConfigurationExtensionKey key, String valueIfNotSet) {
		return m_dataMap.getOrDefault(key, valueIfNotSet);
	}

	public Integer getIntegerValue(ConfigurationExtensionKey key) {
		return getIntegerValue(key, null);
	}

	public Integer getIntegerValue(ConfigurationExtensionKey key, Integer valueIfNotSet) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return valueIfNotSet;
		}

		return Integer.parseInt(stringValue);
	}

	public boolean getBooleanValue(ConfigurationExtensionKey key) {
		return getBooleanValue(key, false);
	}

	public boolean getBooleanValue(ConfigurationExtensionKey key, boolean valueIfNotSet) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return valueIfNotSet;
		}

		return Boolean.parseBoolean(stringValue);
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

		SortedMap<ConfigurationExtensionKey, String> sortedMap = new TreeMap<>(m_dataMap);

		for (Entry<ConfigurationExtensionKey, String> entry : sortedMap.entrySet()) {
			list.add(toString(entry));
		}

		return list;
	}
}
