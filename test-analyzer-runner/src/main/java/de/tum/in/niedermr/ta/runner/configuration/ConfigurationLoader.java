package de.tum.in.niedermr.ta.runner.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.parser.ConfigurationParser;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractStringProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/**
 * Load a configuration from a file.<br/>
 * <br/>
 * 
 * Comments can be specified using {@link COMMENT_START_SEQ_1} or {@link COMMENT_START_SEQ_2}.<br/>
 * A configuration file can extend another one. To do so, the first line must start with <code>extends</code> followed
 * by a space and the path to the configuration file to be inherited. The path is supposed to be relative to the current
 * configuration file.
 */
public class ConfigurationLoader implements FileSystemConstants {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ConfigurationLoader.class);

	private static boolean s_fastFail = false;

	private static final String EMPTY_ROOT_PATH = "";
	public static final String KEYWORD_EXTENDS = "extends";
	public static final String KEY_VALUE_SEPARATOR_SET = "=";
	public static final String KEY_VALUE_SEPARATOR_APPEND = "+=";

	public static final String COMMENT_START_SEQ_1 = "#";
	public static final String COMMENT_START_SEQ_2 = "//";

	private final Configuration m_configuration;
	private final String m_rootPath;

	private ConfigurationLoader(String rootPath) {
		this.m_configuration = new Configuration();
		this.m_rootPath = rootPath;
	}

	public static void setFastFail(boolean fastFail) {
		s_fastFail = fastFail;
	}

	public static boolean isFastFail() {
		return s_fastFail;
	}

	public static Configuration getConfiguration() throws ConfigurationException, FileNotFoundException {
		try {
			ConfigurationLoader loader = new ConfigurationLoader(".");

			// don't close sc, because it will close System.in (and that can't be reopened)
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);

			System.out.println("Path to configuration file:");
			String fileName = sc.nextLine();
			loader.loadFromFile(fileName);

			return loader.m_configuration;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	public static Configuration getConfigurationFromFile(String configurationFile)
			throws ConfigurationException, IOException {
		return getConfigurationFromFile(configurationFile, EMPTY_ROOT_PATH);
	}

	public static Configuration getConfigurationFromFile(String configurationFile, String rootPath)
			throws ConfigurationException, IOException {
		ConfigurationLoader loader = new ConfigurationLoader(rootPath);
		loader.loadFromFile(configurationFile);

		return loader.m_configuration;
	}

	private void loadFromFile(String fileName) throws ConfigurationException, IOException {
		LOGGER.info("Configuration from file ('" + fileName + "' in '" + this.m_rootPath + "')");

		File configFile = new File(fileName);
		String pathToConfiguration;

		if (configFile.isAbsolute()) {
			pathToConfiguration = fileName;
		} else {
			pathToConfiguration = m_rootPath + fileName;
		}

		try {
			ConfigurationParser.parseFromFile(pathToConfiguration, m_configuration);
		} catch (FileNotFoundException ex) {
			LOGGER.info(
					"Assumed absolute path to configuration file: " + new File(pathToConfiguration).getAbsolutePath());
			throw ex;
		}
	}

	public static String[] toArgsArray(Configuration configuration) {
		List<IConfigurationProperty<?>> propertyList = configuration.getAllPropertiesOrdered();

		String[] result = new String[propertyList.size()];

		for (int i = 0; i < result.length; i++) {
			String value = propertyList.get(i).getValueAsString();

			if (value.isEmpty()) {
				value = AbstractStringProperty.PLACEHOLDER_EMPTY;
			}

			result[i] = value;
		}

		return result;
	}

	public static List<String> toFileLines(Configuration configuration, boolean includeDescriptionAsComment) {
		List<String> result = new LinkedList<>();

		result.add(COMMENT_START_SEQ_1 + " AUTOGENERATED");

		for (IConfigurationProperty<?> property : configuration.getAllPropertiesOrdered()) {
			if (includeDescriptionAsComment) {
				result.add(COMMENT_START_SEQ_1 + " " + property.getDescription());
			}

			result.add(property.getName() + KEY_VALUE_SEPARATOR_SET + property.getValueAsString());
		}

		result.addAll(configuration.getDynamicValues().toStringLines());

		return result;
	}

	public static void writeToFile(Configuration configuration, String file) throws IOException {
		TextFileData.writeToFile(file, toFileLines(configuration, false));
	}
}
