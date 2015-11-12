package de.tum.in.niedermr.ta.runner.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.runner.analysis.AnalyzerRunnerInternal;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.parser.ConfigurationParser;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractConfigurationProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractStringProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/**
 * Configurations can be loaded from the arguments, from the console or from a file.<br/>
 * <br/>
 * 
 * <b>Arguments:</b> All properties need to be specified and these need to be in the order of
 * {@link Configuration#getAllPropertiesOrdered()}.<br/>
 * <br/>
 * 
 * <b>Console:</b> The property values will be requested using System.in.<br/>
 * <br/>
 * 
 * <b>Configuration file:</b> The order is not relevant and not all properties need to be specified. If a property is
 * not specified, the default value will be used.<br/>
 * Comments can be specified using {@link COMMENT_START_SEQ_1} or {@link COMMENT_START_SEQ_2}.<br/>
 * A configuration file can extend another one. To do so, the first line must start with 'extends' followed by a space
 * and the path to the configuration file to be inherited. The path is supposed to be relative to the current
 * configuration file.
 *
 */
public class ConfigurationLoader implements FileSystemConstants {
	private static final Logger LOG = AnalyzerRunnerInternal.LOG;

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

	public static Configuration getConfiguration(String[] args, String rootPathForFiles) throws ConfigurationException {
		try {
			ConfigurationLoader loader = new ConfigurationLoader(rootPathForFiles);

			if (args.length != 0) {
				loader.loadFromArgs(args);
			} else {
				loader.loadFromOtherSource();
			}

			return loader.m_configuration;
		} catch (Exception ex) {
			throw new ConfigurationException(ex);
		}
	}

	public static Configuration getConfigurationFromArgs(String[] args) throws ConfigurationException {
		ConfigurationLoader loader = new ConfigurationLoader(EMPTY_ROOT_PATH);
		loader.loadFromArgs(args);

		return loader.m_configuration;
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

	private void loadFromArgs(String[] args) throws ConfigurationException {
		LOG.info("Configuration from args");

		int i = 0;

		for (IConfigurationProperty<?> property : m_configuration.getAllPropertiesOrdered()) {
			String stringValue = CommonUtility.getArgument(args, i, AbstractConfigurationProperty.PLACEHOLDER_DEFAULT);
			property.setValueUnsafe(stringValue);
			i++;
		}
	}

	private void loadFromOtherSource() throws Exception {
		// don't close sc, because it will close System.in (and that can't be reopened)
		Scanner sc = new Scanner(System.in);

		writeToConsole("Path to configuration file (leave it empty to load the information from the console):");
		String input = sc.nextLine();

		if (input.isEmpty()) {
			loadFromConsole(sc);
		} else {
			loadFromFile(input);
		}
	}

	private void loadFromConsole(Scanner scanner) throws ConfigurationException {
		LOG.info("Configuration from console");

		for (IConfigurationProperty<?> property : m_configuration.getAllPropertiesOrdered()) {
			writeToConsole(property.getDescription());
			System.out.print("Set value: ");
			property.setValueUnsafe(scanner.nextLine());
		}
	}

	private void loadFromFile(String fileName) throws IOException, ConfigurationException {
		LOG.info("Configuration from file ('" + fileName + "' in '" + this.m_rootPath + "')");

		String pathToConfiguration;

		if (fileName.charAt(0) == '@') {
			pathToConfiguration = this.m_rootPath + fileName.substring(1) + FILE_EXTENSION_CONFIG;
		} else {
			File configFile = new File(fileName);

			if (configFile.isAbsolute()) {
				pathToConfiguration = fileName;
			} else {
				pathToConfiguration = this.m_rootPath + fileName;
			}
		}

		try {
			ConfigurationParser.parseFromFile(pathToConfiguration, m_configuration);
		} catch (FileNotFoundException ex) {
			LOG.info("Assumed absolute path to configuration file: " + new File(pathToConfiguration).getAbsolutePath());
			throw ex;
		}
	}

	public static String[] toArgsArray(Configuration configuration) {
		List<IConfigurationProperty<?>> propertyList = configuration.getAllPropertiesOrdered();

		String[] result = new String[propertyList.size()];

		for (int i = 0; i < result.length; i++) {
			String value = propertyList.get(i).getValueAsString();
			result[i] = (value.isEmpty() ? AbstractStringProperty.PLACEHOLDER_EMPTY : value);
		}

		return result;
	}

	public static List<String> toFileLines(Configuration configuration) {
		return toFileLines(configuration, false);
	}

	public static List<String> toFileLines(Configuration configuration, boolean includeDescriptionAsComment) {
		List<String> result = new LinkedList<>();

		result.add(COMMENT_START_SEQ_1 + " AUTOGENERATED");

		for (IConfigurationProperty<?> property : configuration.getAllPropertiesOrdered()) {
			if (property.isDeprecated()) {
				continue;
			}

			if (includeDescriptionAsComment) {
				result.add(COMMENT_START_SEQ_1 + " " + property.getDescription());
			}

			result.add(property.getName() + KEY_VALUE_SEPARATOR_SET + property.getValueAsString());
		}

		return result;
	}

	private static void writeToConsole(String output) {
		System.out.println(output);
	}
}
