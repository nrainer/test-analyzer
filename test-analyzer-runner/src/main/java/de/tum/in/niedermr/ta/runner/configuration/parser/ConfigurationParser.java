package de.tum.in.niedermr.ta.runner.configuration.parser;

import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.ConfigurationMigrationFromV1;
import de.tum.in.niedermr.ta.runner.configuration.parser.migration.IConfigurationMigration;
import de.tum.in.niedermr.ta.runner.configuration.property.ConfigurationVersionProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

public class ConfigurationParser extends AbstractConfigurationParser {

	private IConfigurationMigration m_configurationMigration = null;

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

	@Override
	protected String adjustValue(IConfigurationProperty<?> property, String value) {
		if (m_configurationMigration != null) {
			return m_configurationMigration.migrateRawValue(property, value);
		} else {
			return super.adjustValue(property, value);
		}
	}

	@Override
	protected void execConfigurationVersionLoaded(Integer version) {
		if (version == null) {
			LOG.warn(ConfigurationVersionProperty.NAME + " specified with null value.");
			m_configurationMigration = null;
		} else if (version == 1) {
			m_configurationMigration = new ConfigurationMigrationFromV1();
		} else {
			m_configurationMigration = null;
		}
	}
}
