package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.io.Serializable;

/** Key to access an extension property. */
public class ConfigurationExtensionKey implements Serializable, Comparable<ConfigurationExtensionKey> {

	/** Version. */
	private static final long serialVersionUID = 1L;

	public static final String EXTENSION_PROPERTY_PREFIX = "extension.";

	private final String m_name;

	private ConfigurationExtensionKey(String qualifiedName) {
		m_name = qualifiedName;
	}

	public static ConfigurationExtensionKey create(String shortKey) {
		return new ConfigurationExtensionKey(EXTENSION_PROPERTY_PREFIX + shortKey);
	}

	public static ConfigurationExtensionKey parse(String qualifiedName) {
		return new ConfigurationExtensionKey(qualifiedName);
	}

	public String getName() {
		return m_name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConfigurationExtensionKey) {
			return m_name.equals(((ConfigurationExtensionKey) obj).m_name);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return m_name.hashCode();
	}

	@Override
	public int compareTo(ConfigurationExtensionKey o) {
		return m_name.compareTo(o.m_name);
	}
}
