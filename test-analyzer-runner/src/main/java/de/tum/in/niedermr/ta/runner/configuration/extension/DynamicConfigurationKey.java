package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.io.Serializable;

/** Key to access a dynamic configuration property. */
public class DynamicConfigurationKey implements Serializable, Comparable<DynamicConfigurationKey> {

	/** Version. */
	private static final long serialVersionUID = 1L;

	/** Name of the key including the prefix. */
	private final String m_name;

	/** Default value if not set. */
	private final Object m_defaultValue;

	/** Constructor. */
	private DynamicConfigurationKey(String qualifiedName, Object defaultValue) {
		m_name = qualifiedName;
		m_defaultValue = defaultValue;
	}

	/**
	 * Create a new key.
	 * 
	 * @param namespace
	 *            of the key
	 * @param keyWithoutPrefix
	 *            name of the key without the prefix of the namespace
	 * @param defaultValue
	 *            default value if the value is not specified
	 */
	public static DynamicConfigurationKey create(DynamicConfigurationKeyNamespace namespace, String shortKey,
			Object defaultValue) {
		return new DynamicConfigurationKey(namespace.getKeyPrefix() + shortKey, defaultValue);
	}

	/**
	 * Parse a configuration key by its qualified name.<br/>
	 * Warning: The default value will be set to <code>null</code>.
	 */
	public static DynamicConfigurationKey parse(String qualifiedName) {
		return new DynamicConfigurationKey(qualifiedName, null);
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

	/** {@link #m_name} */
	public String getName() {
		return m_name;
	}

	/** {@link #m_defaultValue} */
	@SuppressWarnings("unchecked")
	public <T> T getDefaultValue() {
		return (T) m_defaultValue;
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
