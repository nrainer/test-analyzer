package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import java.util.List;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Migration that acts as container for an ordered list of multiple migrations. */
public class ChainedConfigurationMigration implements IConfigurationMigration {

	private final List<IConfigurationMigration> m_migrations;

	/** Constructor. */
	public ChainedConfigurationMigration(List<IConfigurationMigration> migrations) {
		m_migrations = migrations;
	}

	/** {@inheritDoc} */
	@Override
	public String migrateKey(String key) {
		String result = key;

		for (IConfigurationMigration migration : m_migrations) {
			result = migration.migrateKey(result);
		}

		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String migrateRawValue(IConfigurationProperty<?> property, String value) {
		String result = value;

		for (IConfigurationMigration migration : m_migrations) {
			result = migration.migrateRawValue(property, result);
		}

		return result;
	}
}
