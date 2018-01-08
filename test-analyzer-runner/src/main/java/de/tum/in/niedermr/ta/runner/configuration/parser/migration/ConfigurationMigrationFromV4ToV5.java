package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Migration for the configuration from version 4 to 5. */
class ConfigurationMigrationFromV4ToV5 implements IConfigurationMigration {

	/** {@inheritDoc} */
	@Override
	public String migrateKey(String key) {
		switch (key) {
		case "extension.converter.pit.unrollMultipleTestcases.enabled":
			return "extension.converter.pit.mutationMatrix.enabled";
		case "extension.converter.pit.unrollMultipleTestcases.separator":
			return "extension.converter.pit.mutationMatrix.testcaseSeparator";
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
