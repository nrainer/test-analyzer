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

	public String getStringValue(DynamicConfigurationKey key) {
		return getStringValue(key, null);
	}

	public String getStringValue(DynamicConfigurationKey key, String valueIfNotSet) {
		return m_dataMap.getOrDefault(key, valueIfNotSet);
	}

	public Integer getIntegerValue(DynamicConfigurationKey key) {
		return getIntegerValue(key, null);
	}

	public Integer getIntegerValue(DynamicConfigurationKey key, Integer valueIfNotSet) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return valueIfNotSet;
		}

		return Integer.parseInt(stringValue);
	}

	public boolean getBooleanValue(DynamicConfigurationKey key) {
		return getBooleanValue(key, false);
	}

	public boolean getBooleanValue(DynamicConfigurationKey key, boolean valueIfNotSet) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return valueIfNotSet;
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
