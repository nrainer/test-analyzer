package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Migration for the configuration from version 3 to 4. */
class ConfigurationMigrationFromV3ToV4 implements IConfigurationMigration {

	/** {@inheritDoc} */
	@Override
	public String migrateKey(String key) {
		switch (key) {
		case "testWorkflows":
			return "workflows";
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
