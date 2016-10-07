package de.tum.in.niedermr.ta.runner.configuration.extension;

import java.io.Serializable;

/** Key to access a dynamic configuration property. */
public class DynamicConfigurationKey implements Serializable, Comparable<DynamicConfigurationKey> {

	/** Version. */
	private static final long serialVersionUID = 1L;

	protected static final String EXTENSION_PROPERTY_PREFIX = "extension.";

	private final String m_name;

	/** Constructor. */
	private DynamicConfigurationKey(String qualifiedName) {
		m_name = qualifiedName;
	}

	public static DynamicConfigurationKey create(String shortKey) {
		return new DynamicConfigurationKey(EXTENSION_PROPERTY_PREFIX + shortKey);
	}

	public static DynamicConfigurationKey parse(String qualifiedName) {
		return new DynamicConfigurationKey(qualifiedName);
	}

	/** Return true if the key is a dynamic configuration key. */
	public static boolean isDynamicConfigurationKey(String key) {
		return key.startsWith(DynamicConfigurationKey.EXTENSION_PROPERTY_PREFIX);
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
