package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.io.Serializable;

/** Key to access a dynamic configuration property. */
public class DynamicConfigurationKey implements Serializable, Comparable<DynamicConfigurationKey> {

	/** Version. */
	private static final long serialVersionUID = 1L;

	/** Name of the key including the prefix. */
	private final String m_name;

	/** Constructor. */
	private DynamicConfigurationKey(String qualifiedName) {
		m_name = qualifiedName;
	}

	public static DynamicConfigurationKey create(DynamicConfigurationKeyNamespace namespace, String shortKey) {
		return new DynamicConfigurationKey(namespace.getKeyPrefix() + shortKey);
	}

	public static DynamicConfigurationKey parse(String qualifiedName) {
		return new DynamicConfigurationKey(qualifiedName);
	}

	/** Return true if the key is a dynamic configuration key. */
	public static boolean isDynamicConfigurationKey(String key) {
		for (DynamicConfigurationKeyNamespace namespace : DynamicConfigurationKeyNamespace.values()) {
			if (key.startsWith(namespace.getKeyPrefix())) {
				return true;
			}
		}

		return false;
	}

	public String getName() {
		return m_name;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getName();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DynamicConfigurationKey) {
			return m_name.equals(((DynamicConfigurationKey) obj).m_name);
		}

		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return m_name.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public int compareTo(DynamicConfigurationKey o) {
		return m_name.compareTo(o.m_name);
	}
}
