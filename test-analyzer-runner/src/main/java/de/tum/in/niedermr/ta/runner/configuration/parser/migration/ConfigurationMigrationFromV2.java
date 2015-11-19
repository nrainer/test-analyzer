package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

public class ConfigurationMigrationFromV2 implements IConfigurationMigration {

	@Override
	public String migrateKey(String key) {
		switch (key) {
		case "testClassesToSkip":
			return "testClassExcludes";
		default:
			return key;
		}
	}

	@Override
	public String migrateRawValue(IConfigurationProperty<?> property, String value0) {
		return value0;
	}
}
