package de.tum.in.niedermr.ta.runner.configuration.parser;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.ConfigurationMigrationManager;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.IConfigurationMigration;
import de.tum.in.niedermr.ta.runner.configuration.property.ConfigurationVersionProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Parser for {@link Configuration}. */
public class ConfigurationParser extends AbstractConfigurationParser<Configuration> {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ConfigurationParser.class);

	private IConfigurationMigration m_configurationMigration = null;

	public static Configuration parseFromFile(String pathToConfigFile) throws IOException, ConfigurationException {
		ConfigurationParser parser = new ConfigurationParser();
		parser.parse(pathToConfigFile);
		return parser.getConfiguration();
	}

	/** {@inheritDoc} */
	@Override
	protected Configuration createNewConfiguration() {
		return new Configuration();
	}

	/** {@inheritDoc} */
	@Override
	protected IConfigurationProperty<?> getPropertyByKey(String key0) {
		String key = key0;

		if (m_configurationMigration != null) {
			key = m_configurationMigration.migrateKey(key);
		}

		return super.getPropertyByKey(key);
	}

	/** {@inheritDoc} */
	@Override
	protected String adjustValue(IConfigurationProperty<?> property, String value) {
		if (m_configurationMigration != null) {
			return m_configurationMigration.migrateRawValue(property, value);
		}

		return super.adjustValue(property, value);
	}

	/** {@inheritDoc} */
	@Override
	protected void execConfigurationVersionLoaded(Integer version) {
		if (version == Configuration.CURRENT_VERSION) {
			// up to date --> no migration needed
			m_configurationMigration = null;
		} else if (version == null) {
			LOGGER.warn(ConfigurationVersionProperty.NAME + " specified with null value.");
			m_configurationMigration = null;
		} else {
			m_configurationMigration = ConfigurationMigrationManager.createMigration(version);
		}
	}
}
