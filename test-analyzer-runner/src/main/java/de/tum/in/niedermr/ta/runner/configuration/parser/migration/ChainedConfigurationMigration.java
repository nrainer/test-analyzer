package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import java.util.Arrays;
import java.util.List;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

public class ChainedConfigurationMigration implements IConfigurationMigration {

	private final List<IConfigurationMigration> m_migrationsList;

	public ChainedConfigurationMigration(IConfigurationMigration... migrations) {
		m_migrationsList = Arrays.asList(migrations);
	}

	@Override
	public String migrateKey(String key) {
		String result = key;

		for (IConfigurationMigration migration : m_migrationsList) {
			result = migration.migrateKey(result);
		}

		return result;
	}

	@Override
	public String migrateRawValue(IConfigurationProperty<?> property, String value) {
		String result = value;

		for (IConfigurationMigration migration : m_migrationsList) {
			result = migration.migrateRawValue(property, result);
		}

		return result;
	}
}
