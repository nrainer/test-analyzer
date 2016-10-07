package de.tum.in.niedermr.ta.runner.configuration.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.ChainedConfigurationMigration;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.ConfigurationMigrationFromV1;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.ConfigurationMigrationFromV2;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.ConfigurationMigrationFromV3;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.IConfigurationMigration;
import de.tum.in.niedermr.ta.runner.configuration.property.ConfigurationVersionProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Parser for {@link Configuration}. */
public class ConfigurationParser extends AbstractConfigurationParser {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ConfigurationParser.class);

	private IConfigurationMigration m_configurationMigration = null;

	/** Constructor. */
	protected ConfigurationParser(Configuration result) {
		super(result);
	}

	public static Configuration parseFromFile(String pathToConfigFile) throws IOException, ConfigurationException {
		Configuration configuration = new Configuration();
		parseFromFile(pathToConfigFile, configuration);
		return configuration;
	}

	public static void parseFromFile(String pathToConfigFile, Configuration result)
			throws IOException, ConfigurationException {
		ConfigurationParser parser = new ConfigurationParser(result);
		parser.parse(pathToConfigFile);
	}

	/** {@inheritDoc} */
	@Override
	protected IConfigurationProperty<?> getPropertyByKey(String key0) {
		String key;

		if (m_configurationMigration != null) {
			key = m_configurationMigration.migrateKey(key0);
		} else {
			key = key0;
		}

		return super.getPropertyByKey(key);
	}

	/** {@inheritDoc} */
	@Override
	protected String adjustValue(IConfigurationProperty<?> property, String value) {
		if (m_configurationMigration != null) {
			return m_configurationMigration.migrateRawValue(property, value);
		} else {
			return super.adjustValue(property, value);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execConfigurationVersionLoaded(Integer version) {
		if (version == null) {
			LOGGER.warn(ConfigurationVersionProperty.NAME + " specified with null value.");
			m_configurationMigration = null;
		} else if (version == Configuration.CURRENT_VERSION) {
			m_configurationMigration = null;
		} else {
			m_configurationMigration = createMigrations(version);
		}
	}

	/** Create the needed migrations. */
	private IConfigurationMigration createMigrations(int version) {
		List<IConfigurationMigration> migrations = new ArrayList<>();

		if (version <= 1) {
			migrations.add(new ConfigurationMigrationFromV1());
		}

		if (version <= 2) {
			migrations.add(new ConfigurationMigrationFromV2());
		}

		if (version <= 3) {
			migrations.add(new ConfigurationMigrationFromV3());
		}

		return new ChainedConfigurationMigration(migrations);
	}
}
