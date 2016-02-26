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
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoader;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.ConfigurationVersionProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

abstract class AbstractConfigurationParser {
	private static final Logger LOG = LogManager.getLogger(AbstractConfigurationParser.class);

	private ConfigurationPropertyMap m_propertyMap;
	private Set<IConfigurationProperty<?>> m_processedPropertiesInCurrentFile;

	protected AbstractConfigurationParser(AbstractConfiguration result) {
		this.m_propertyMap = new ConfigurationPropertyMap(result);
		this.m_processedPropertiesInCurrentFile = new HashSet<>();
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
		if (ConfigurationLoader.isFastFail()) {
			throw new ConfigurationException("Invalid line: " + line);
		} else {
			LOG.warn("Skipping invalid log file line: " + line);
		}
	}

	protected List<String> getFileContent(String pathToConfigFile) throws IOException {
		return TextFileData.readFromFile(pathToConfigFile);
	}

	private boolean isInheritLine(String line) {
		return line.trim().startsWith(ConfigurationLoader.KEYWORD_EXTENDS + " ");
	}

	/**
	 * True, if the line is not empty or a comment.
	 */
	private boolean isLineWithContent(String line) {
		return !(line.trim().isEmpty() || line.startsWith(ConfigurationLoader.COMMENT_START_SEQ_1)
				|| line.startsWith(ConfigurationLoader.COMMENT_START_SEQ_2));
	}

	private void parseLine(String line) throws ConfigurationException {
		final AbstractConfigurationParser.LineType lineType = getLineType(line);
		final String[] lineParts = line.split(Pattern.quote(lineType.m_separator));

		final String key = getKeyOfLine(lineParts);
		final IConfigurationProperty<?> property = getPropertyByKey(key);

		final String stringValue = adjustValue(property, getValueOfLine(lineParts));

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
			throw new IllegalStateException();
		}
	}

	private String getKeyOfLine(String[] lineParts) {
		return lineParts[0].trim();
	}

	private String getValueOfLine(String[] lineParts) {
		return lineParts.length > 1 ? lineParts[1].trim() : "";
	}

	private void checkIfAlreadySet(IConfigurationProperty<?> property, String line) throws ConfigurationException {
		if (m_processedPropertiesInCurrentFile.contains(property)) {
			String msg = "Overwriting property which was already set: " + line;

			if (ConfigurationLoader.isFastFail()) {
				throw new ConfigurationException(property, msg);
			} else {
				LOG.warn(msg);
			}
		}
	}

	private void handleInheritance(String inheritLine, String pathToCurrentConfiguration)
			throws ConfigurationException {
		try {
			File currentConfigurationFile = new File(pathToCurrentConfiguration);
			String pathToInheritedConfiguration = inheritLine.replace(ConfigurationLoader.KEYWORD_EXTENDS, "").trim();

			if (currentConfigurationFile.getParent() != null) {
				pathToInheritedConfiguration = FileUtility.prefixFileNameIfNotAbsolute(pathToInheritedConfiguration,
						currentConfigurationFile.getParent() + FileSystemConstants.PATH_SEPARATOR);
			}

			parse(pathToInheritedConfiguration);

			LOG.info("Configuration '" + currentConfigurationFile.getName() + "' inherits '"
					+ pathToInheritedConfiguration + "'");
		} catch (ConfigurationException ex) {
			throw new ConfigurationException("Error in inherited configuration");
		} catch (Throwable t) {
			throw new ConfigurationException(new IllegalStateException("Incorrect inheritance: " + inheritLine));
		}
	}

	private enum LineType {
		SET(ConfigurationLoader.KEY_VALUE_SEPARATOR_SET), APPEND(ConfigurationLoader.KEY_VALUE_SEPARATOR_APPEND);

		private final String m_separator;

		private LineType(String separator) {
			this.m_separator = separator;
		}
	}
}