package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;

/** Manages the migration of {@link Configuration}s. */
public class ConfigurationMigrationManager {

	private static final List<IConfigurationMigration> ALL_MIGRATIONS = new ArrayList<>();

	static {
		ALL_MIGRATIONS.add(new ConfigurationMigrationFromV1());
		ALL_MIGRATIONS.add(new ConfigurationMigrationFromV2());
		ALL_MIGRATIONS.add(new ConfigurationMigrationFromV3());
		ALL_MIGRATIONS.add(new ConfigurationMigrationFromV4());
	}

	/**
	 * Create the needed migration that may consist of multiple migrations steps to update a configuration to the recent
	 * state.
	 */
	public static ChainedConfigurationMigration createAggregatedMigrationWithRelevantSteps(int currentVersion) {
		List<IConfigurationMigration> migrationsToApply = new ArrayList<>();

		for (IConfigurationMigration migrationStep : ALL_MIGRATIONS) {
			if (currentVersion <= migrationStep.getFromVersion()) {
				migrationsToApply.add(migrationStep);
			}
		}

		return new ChainedConfigurationMigration(migrationsToApply);
	}
}
