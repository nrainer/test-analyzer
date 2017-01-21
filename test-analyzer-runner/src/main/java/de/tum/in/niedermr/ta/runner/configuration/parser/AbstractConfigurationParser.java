package de.tum.in.niedermr.ta.runner.configuration.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.common.util.FileUtility;
import de.tum.in.niedermr.ta.runner.configuration.AbstractConfiguration;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.property.ConfigurationVersionProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

abstract class AbstractConfigurationParser<T extends AbstractConfiguration> {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractConfigurationParser.class);

	private static boolean s_fastFail = false;

	private T m_configuration;
	private ConfigurationPropertyMap m_propertyMap;
	private Set<IConfigurationProperty<?>> m_processedPropertiesInCurrentFile;

	/** Constructor. */
	protected AbstractConfigurationParser() {
		m_configuration = createNewConfiguration();
		m_propertyMap = new ConfigurationPropertyMap(m_configuration);
		m_processedPropertiesInCurrentFile = new HashSet<>();
	}

	/** For test code only. */
	public static void setFastFail(boolean fastFail) {
		s_fastFail = fastFail;
	}

	protected abstract T createNewConfiguration();

	protected T getConfiguration() {
		return m_configuration;
	}

	protected void parse(String pathToConfigFile) throws IOException, ConfigurationException {
		final List<String> lines = getFileContent(pathToConfigFile);

		if (lines.size() > 0) {
			String firstLine = lines.get(0);

			if (isInheritLine(firstLine)) {
				handleInheritance(firstLine, pathToConfigFile);
				lines.remove(0);
			}
		}

		m_processedPropertiesInCurrentFile.clear();

		for (String currentLine : lines) {
			if (!isLineWithContent(currentLine)) {
				continue;
			}

			try {
				parseLine(currentLine);
			} catch (ArrayIndexOutOfBoundsException | IllegalStateException | NullPointerException
					| ConfigurationException ex) {
				handleParseLineException(currentLine);
			}
		}

		execAfterParse();
	}

	protected void execAfterParse() {
		ConfigurationVersionProperty configurationVersionProperty = (ConfigurationVersionProperty) getPropertyByKey(
				ConfigurationVersionProperty.NAME);
		configurationVersionProperty.setConfigurationVersionOfProgram();
	}

	private void handleParseLineException(String line) throws ConfigurationException {
		if (s_fastFail) {
			throw new ConfigurationException("Invalid line: " + line);
		} else {
			LOGGER.warn("Skipping invalid log file line: " + line);
		}
	}

	protected List<String> getFileContent(String pathToConfigFile) throws IOException {
		return TextFileData.readFromFile(pathToConfigFile);
	}

	private boolean isInheritLine(String line) {
		return line.trim().startsWith(IConfigurationTokens.KEYWORD_EXTENDS + " ");
	}

	/**
	 * True, if the line is not empty or a comment.
	 */
	private boolean isLineWithContent(String line) {
		return !(line.trim().isEmpty() || line.startsWith(IConfigurationTokens.COMMENT_START_SEQ_1)
				|| line.startsWith(IConfigurationTokens.COMMENT_START_SEQ_2));
	}

	private void parseLine(String line) throws ConfigurationException {
		AbstractConfigurationParser.LineType lineType = getLineType(line);
		String[] lineParts = line.split(Pattern.quote(lineType.m_separator));

		String key = getKeyOfLine(lineParts);
		String rawValue = getValueOfLine(lineParts);

		if (DynamicConfigurationKey.isDynamicConfigurationKey(key)) {
			handleDynamicProperty(lineType, key, rawValue);
		} else {
			handleBuiltInProperty(line, lineType, key, rawValue);
		}
	}

	private void handleBuiltInProperty(String line, LineType lineType, String key, String rawValue)
			throws ConfigurationException {
		final IConfigurationProperty<?> property = getPropertyByKey(key);

		final String stringValue = adjustValue(property, rawValue);

		if (property == null) {
			throw new IllegalStateException("Property not found: " + key);
		}

		if (lineType == LineType.SET) {
			checkIfAlreadySet(property, line);
			property.setValueUnsafe(stringValue);
		} else if (lineType == LineType.APPEND) {
			property.setValueUnsafe(property.getValueAsString() + stringValue);
		}

		m_processedPropertiesInCurrentFile.add(property);

		if (property instanceof ConfigurationVersionProperty) {
			execConfigurationVersionLoaded(((ConfigurationVersionProperty) property).getValue());
		}
	}

	private void handleDynamicProperty(LineType lineType, String key, String rawValue) {
		if (lineType != LineType.SET) {
			throw new IllegalStateException("Dynamic properties only support assignments with " + lineType.m_separator);
		}

		m_configuration.getDynamicValues().setRawValue(DynamicConfigurationKey.parse(key), rawValue);
	}

	/**
	 * May not be invoked (if not specified in the file) or be invoked multiple times (configuration inheritance).
	 * 
	 * @param version
	 */
	protected void execConfigurationVersionLoaded(Integer version) {
		// NOP
	}

	protected IConfigurationProperty<?> getPropertyByKey(String key) {
		return m_propertyMap.getPropertyByKey(key);
	}

	/**
	 * Can be used to migrate a value.
	 * 
	 * @param property
	 * @param value
	 */
	protected String adjustValue(IConfigurationProperty<?> property, String value) {
		return value;
	}

	private AbstractConfigurationParser.LineType getLineType(String line) throws IllegalStateException {
		if (line.contains(LineType.APPEND.m_separator)) {
			return LineType.APPEND;
		} else if (line.contains(LineType.SET.m_separator)) {
			return LineType.SET;
		} else {
			throw new IllegalStateException("Unknown line type");
		}
	}

	private String getKeyOfLine(String[] lineParts) {
		return lineParts[0].trim();
	}

	private String getValueOfLine(String[] lineParts) {
		if (lineParts.length <= 1) {
			return "";
		}

		return lineParts[1].trim();
	}

	private void checkIfAlreadySet(IConfigurationProperty<?> property, String line) throws ConfigurationException {
		if (m_processedPropertiesInCurrentFile.contains(property)) {
			String msg = "Overwriting property which was already set: " + line;

			if (s_fastFail) {
				throw new ConfigurationException(property, msg);
			} else {
				LOGGER.warn(msg);
			}
		}
	}

	private void handleInheritance(String inheritLine, String pathToCurrentConfiguration)
			throws ConfigurationException {
		try {
			File currentConfigurationFile = new File(pathToCurrentConfiguration);
			String pathToInheritedConfiguration = inheritLine.replace(IConfigurationTokens.KEYWORD_EXTENDS, "").trim();

			if (currentConfigurationFile.getParent() != null) {
				pathToInheritedConfiguration = FileUtility.prefixFileNameIfNotAbsolute(pathToInheritedConfiguration,
						currentConfigurationFile.getParent() + FileSystemConstants.PATH_SEPARATOR);
			}

			parse(pathToInheritedConfiguration);

			LOGGER.info("Configuration '" + currentConfigurationFile.getName() + "' inherits '"
					+ pathToInheritedConfiguration + "'");
		} catch (ConfigurationException ex) {
			throw new ConfigurationException("Error in inherited configuration");
		} catch (Throwable t) {
			throw new ConfigurationException(new IllegalStateException("Incorrect inheritance: " + inheritLine, t));
		}
	}

	/** Line type used to separate the key and value in the configuration file. */
	private enum LineType {
		SET(IConfigurationTokens.KEY_VALUE_SEPARATOR_SET), APPEND(IConfigurationTokens.KEY_VALUE_SEPARATOR_APPEND);

		private final String m_separator;

		private LineType(String separator) {
			this.m_separator = separator;
		}
	}
}