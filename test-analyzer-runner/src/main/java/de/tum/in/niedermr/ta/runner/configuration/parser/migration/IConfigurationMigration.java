package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

public interface IConfigurationMigration {

	/** Get the version until which this migration is necessary. */
	int getFromVersion();

	String migrateKey(String key);

	String migrateRawValue(IConfigurationProperty<?> property, String value);
}
