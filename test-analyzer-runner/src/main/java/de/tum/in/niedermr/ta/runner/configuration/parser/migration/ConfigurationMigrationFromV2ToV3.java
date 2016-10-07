package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Migration for the configuration from version 2 to 3. */
class ConfigurationMigrationFromV2ToV3 implements IConfigurationMigration {

	/** {@inheritDoc} */
	@Override
	public String migrateKey(String key) {
		switch (key) {
		case "testClassesToSkip":
			return "testClassExcludes";
		default:
			return key;
		}
	}

	/** {@inheritDoc} */
	@Override
	public String migrateRawValue(IConfigurationProperty<?> property, String value0) {
		return value0;
	}
}
