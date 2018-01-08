package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;

/** Manages the migration of {@link Configuration}s. */
public class ConfigurationMigrationManager {

	/**
	 * Create the needed migration that may consist of multiple migrations steps to update a configuration to the recent
	 * state.
	 */
	public static IConfigurationMigration createMigration(int version) {
		List<IConfigurationMigration> migrations = new ArrayList<>();

		if (version <= 1) {
			migrations.add(new ConfigurationMigrationFromV1ToV2());
		}

		if (version <= 2) {
			migrations.add(new ConfigurationMigrationFromV2ToV3());
		}

		if (version <= 3) {
			migrations.add(new ConfigurationMigrationFromV3ToV4());
		}

		if (version <= 4) {
			migrations.add(new ConfigurationMigrationFromV4ToV5());
		}

		return new ChainedConfigurationMigration(migrations);
	}
}
