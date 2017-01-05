package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractConfigurationProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

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

	/** Return true if the key is specified in the configuration. */
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

	/**
	 * Get the value of the specified key as String. Return the default value if no value is specified.
	 */
	public String getStringValue(DynamicConfigurationKey key) {
		return getStringValue(key, key.getDefaultValue());
	}

	/**
	 * Get the raw value of the specified key or the specified default value if no value is specified.
	 */
	private String getStringValue(DynamicConfigurationKey key, String defaultValue) {
		return m_dataMap.getOrDefault(key, defaultValue);
	}

	/**
	 * Get the value of the specified key as Integer. Return the default value if no value is specified.
	 */
	public Integer getIntegerValue(DynamicConfigurationKey key) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return key.getDefaultValue();
		}

		return Integer.parseInt(stringValue.trim());
	}

	/**
	 * Get the value of the specified key as Boolean. Return the default value if no value is specified.
	 */
	public boolean getBooleanValue(DynamicConfigurationKey key) {
		String stringValue = getStringValue(key, null);

		if (stringValue == null) {
			return key.getDefaultValue();
		}

		return Boolean.parseBoolean(stringValue.trim());
	}

	/** Get the value by filling it into the provided property. */
	public <T extends IConfigurationProperty<?>> T getValueAsProperty(DynamicConfigurationKey key, T propertyToFill)
			throws ConfigurationException {
		String stringValue = getStringValue(key);

		if (stringValue == null) {
			propertyToFill.setDefault();
		} else {
			propertyToFill.setValueUnsafe(stringValue);
		}

		return propertyToFill;
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
