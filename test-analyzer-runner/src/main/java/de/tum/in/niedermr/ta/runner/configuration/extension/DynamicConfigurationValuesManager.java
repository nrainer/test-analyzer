package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractConfigurationProperty;

/** Value manager for dynamic configuration properties. */
public class DynamicConfigurationValuesManager {

	private final Map<DynamicConfigurationKey, String> m_dataMap;

	public DynamicConfigurationValuesManager() {
		m_dataMap = new HashMap<>();
	}

	public void setRawValue(DynamicConfigurationKey key, String value) {
		m_dataMap.put(key, value);
	}

	public void removeEntry(DynamicConfigurationKey key) {
		m_dataMap.remove(key);
	}

	public boolean isSet(DynamicConfigurationKey key) {
		return m_dataMap.containsKey(key);
	}

	/**
	 * Throws an exception if the value of the specified key is not specified.<br/>
	 * Note that default values are not considered.
	 */
	public void requireValueIsSet(DynamicConfigurationKey key) {
		if (!isSet(key)) {
			throw new IllegalStateException("Configuration key '" + key.getName() + "' is required but not specified.");
		}
	}

	public String getStringValue(DynamicConfigurationKey key) {
		return getStringValue(key, key.getDefaultValue());
	}

	private String getStringValue(DynamicConfigurationKey key, String defaultValue) {
		return m_dataMap.getOrDefault(key, defaultValue);
	}

	public Integer getIntegerValue(DynamicConfigurationKey key) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return key.getDefaultValue();
		}

		return Integer.parseInt(stringValue);
	}

	public boolean getBooleanValue(DynamicConfigurationKey key) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return key.getDefaultValue();
		}

		return Boolean.parseBoolean(stringValue);
	}

	/** {@inheritDoc} */
	@Override
	public final String toString() {
		return toStringLines().toString();
	}

	private String toString(Entry<DynamicConfigurationKey, String> entry) {
		String propertyName = entry.getKey().getName();
		return AbstractConfigurationProperty.toString(propertyName, entry.getValue());
	}

	public List<String> toStringLines() {
		List<String> list = new ArrayList<>();

		SortedMap<DynamicConfigurationKey, String> sortedMap = new TreeMap<>(m_dataMap);

		for (Entry<DynamicConfigurationKey, String> entry : sortedMap.entrySet()) {
			list.add(toString(entry));
		}

		return list;
	}
}
