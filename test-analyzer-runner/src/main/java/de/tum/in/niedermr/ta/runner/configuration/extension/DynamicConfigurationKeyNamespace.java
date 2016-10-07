package de.tum.in.niedermr.ta.runner.configuration.extension;

/** Namespace for the dynamic configuration keys. */
public enum DynamicConfigurationKeyNamespace {

	/** Namespace for keys referring advanced settings. */
	ADVANCED("advanced."),

	/** Namespace for keys used by extensions. */
	EXTENSION("extension.");

	private final String m_keyPrefix;

	/** Constructor. */
	private DynamicConfigurationKeyNamespace(String keyPrefix) {
		m_keyPrefix = keyPrefix;
	}

	/** {@link #m_keyPrefix} */
	public String getKeyPrefix() {
		return m_keyPrefix;
	}
}