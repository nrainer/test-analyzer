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
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.core.common.util.FileUtility;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.parser.ConfigurationParser;
import de.tum.in.niedermr.ta.runner.configuration.parser.IConfigurationTokens;
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
public class ConfigurationManager implements FileSystemConstants {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ConfigurationManager.class);

	/** Default configuration file name ending. */
	private static final String DEFAULT_CONFIGURATION_FILE_ENDING = ".config";
	/** Default configuration file name. */
	private static final String DEFAULT_CONFIGURATION_FILE_NAME = "analysis" + DEFAULT_CONFIGURATION_FILE_ENDING;

	/** Constructor. */
	private ConfigurationManager() {
		// NOP
	}

	public static Configuration loadConfiguration() throws ConfigurationException, FileNotFoundException {
		try {
			// don't close sc, because it will close System.in (and that can't be reopened)
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);

			System.out.println("Path to configuration file:");
			String fileName = sc.nextLine();
			return loadConfigurationFromFile(fileName, ".");
		} catch (FileNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	public static Configuration loadConfigurationFromFile(String configurationFileName)
			throws ConfigurationException, IOException {
		return loadConfigurationFromFile(configurationFileName, "");
	}

	/**
	 * Load the configuration from a file.
	 * 
	 * @param configurationPath
	 *            path to the configuration file
	 * @param rootPath
	 *            path which will be added as prefix to the configuration path if it is not absolute
	 */
	public static Configuration loadConfigurationFromFile(String configurationPath, String rootPath)
			throws ConfigurationException, IOException {
		LOGGER.info("Configuration from file ('" + configurationPath + "' in '" + rootPath + "')");

		String adjustedConfigurationPath = resolveAdjustedConfigurationPath(configurationPath, rootPath);

		try {
			return ConfigurationParser.parseFromFile(adjustedConfigurationPath);
		} catch (FileNotFoundException ex) {
			LOGGER.info("Assumed absolute path to configuration file: "
					+ new File(adjustedConfigurationPath).getAbsolutePath());
			throw ex;
		}
	}

	/**
	 * Resolve the adjusted path to the configuration. <br/>
	 * Completes incomplete paths: if the path is a folder <code>analysis.config</code> will be appended, if the path
	 * has no file ending <code>.config</code> will be appended.
	 */
	protected static String resolveAdjustedConfigurationPath(String configurationPath, String rootPath) {
		File configurationFile = new File(configurationPath);
		String adjustedConfigurationPath = configurationPath;

		if (!configurationFile.isAbsolute()) {
			adjustedConfigurationPath = rootPath + adjustedConfigurationPath;
		}

		if (FileUtility.endsWithPathSeparator(adjustedConfigurationPath)) {
			// path denotes a folder
			adjustedConfigurationPath = FileUtility.ensurePathEndsWithPathSeparator(adjustedConfigurationPath,
					FileSystemConstants.PATH_SEPARATOR) + DEFAULT_CONFIGURATION_FILE_NAME;
		}

		if (!adjustedConfigurationPath.contains(FileSystemConstants.FILE_EXTENSION_SEPARATOR)) {
			// path does not contain a file ending
			adjustedConfigurationPath += DEFAULT_CONFIGURATION_FILE_ENDING;
		}

		return adjustedConfigurationPath;
	}

	public static List<String> toFileLines(Configuration configuration, boolean includeDescriptionAsComment) {
		List<String> result = new LinkedList<>();

		result.add(IConfigurationTokens.COMMENT_START_SEQ_1 + " AUTOGENERATED");

		for (IConfigurationProperty<?> property : configuration.getAllPropertiesOrdered()) {
			if (includeDescriptionAsComment) {
				result.add(IConfigurationTokens.COMMENT_START_SEQ_1 + " " + property.getDescription());
			}

			result.add(property.getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + property.getValueAsString());
		}

		result.addAll(configuration.getDynamicValues().toStringLines());

		return result;
	}

	public static void writeToFile(Configuration configuration, String file) throws IOException {
		TextFileUtility.writeToFile(file, toFileLines(configuration, false));
	}
}
