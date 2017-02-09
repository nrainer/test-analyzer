package de.tum.in.niedermr.ta.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.conqat.lib.commons.filesystem.FileSystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationManager;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationValuesManager;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.runner.factory.DefaultFactory;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;
import de.tum.in.niedermr.ta.test.integration.jacoco.DefaultFactoryWithJaCoCoRecording;
import de.tum.in.niedermr.ta.test.integration.jacoco.ProcessExecutionWithJaCoCo;

/** Base class for integration tests. */
public abstract class AbstractIntegrationTest implements IntegrationTestConstants, FileSystemConstants {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractIntegrationTest.class);

	private static final String TEST_WORKING_AREA = "./src/test/temp/";
	private static final String TEST_DATA_FOLDER = "./src/test/data/";
	private static final String TEST_COVERAGE_FOLDER = "./src/test/coverage/";
	private static final String TEST_DATA_COMMON_FOLDER = TEST_DATA_FOLDER + "common/";
	private static final String TEST_WORKING_AREA_TO_ROOT = "../../../";

	private static final String FOLDER_EXPECTED = "expected/";
	private static final String FOLDER_CONFIGURATION = "configuration/";
	private static final String FOLDER_OUTPUT = "result/";
	private static final String FOLDER_LOG = "logs/";

	private static final String FILE_NAME_CONFIGURATION = "config" + FILE_EXTENSION_CONFIG;

	private Configuration m_configuration;
	private final String m_systemTestName;
	private boolean m_wasSuccessful;

	/** Constructor. */
	public AbstractIntegrationTest() {
		m_systemTestName = this.getClass().getSimpleName().toLowerCase();
	}

	/** Before each test. */
	@Before
	public void beforeTest() throws IOException, ConfigurationException {
		FileSystemUtils.ensureDirectoryExists(new File(getSpecificFolderTestWorkingArea()));

		loadConfiguration();
		setUpJaCoCoIfAvailable();
	}

	private void loadConfiguration() throws ConfigurationException, IOException {
		String configurationFileName = getSpecificFolderTestData() + FOLDER_CONFIGURATION + FILE_NAME_CONFIGURATION;

		m_configuration = ConfigurationManager.loadConfigurationFromFile(configurationFileName);
		m_configuration.getWorkingFolder().setValue(getSpecificFolderTestWorkingArea());
	}

	private void setUpJaCoCoIfAvailable() throws IOException {
		DynamicConfigurationValuesManager dynamicConfigurationValues = m_configuration.getDynamicValues();

		if (!dynamicConfigurationValues
				.getBooleanValue(ProcessExecutionWithJaCoCo.CONFIGURATION_KEY_PATH_TO_JACOCO_ENABLED)) {
			// no coverage recording
			return;
		}

		if (!DefaultFactory.class.getName().equals(m_configuration.getFactoryClass().getValue())) {
			throw new IllegalArgumentException("No custom factory must be used if code coverage shall be recorded.");
		}

		// replace the default factory with a modified one
		m_configuration.getFactoryClass().setValue(DefaultFactoryWithJaCoCoRecording.class);

		if (!Files.exists(Paths.get(dynamicConfigurationValues
				.getStringValue(ProcessExecutionWithJaCoCo.CONFIGURATION_KEY_PATH_TO_JACOCO_AGENT)))) {
			dynamicConfigurationValues.setRawValue(ProcessExecutionWithJaCoCo.CONFIGURATION_KEY_PATH_TO_JACOCO_ENABLED,
					Boolean.FALSE.toString());
			LOGGER.warn("No coverage recording because the path to the JaCoCo agent is not valid.");
			return;
		}

		String outputFilePath = dynamicConfigurationValues
				.getStringValue(ProcessExecutionWithJaCoCo.CONFIGURATION_KEY_PATH_TO_JACOCO_OUTPUT_FOLDER);

		if (outputFilePath.isEmpty()) {
			File outputFile = new File(getSpecificFolderTestCoverage());
			FileSystemUtils.ensureDirectoryExists(outputFile);
			dynamicConfigurationValues.setRawValue(
					ProcessExecutionWithJaCoCo.CONFIGURATION_KEY_PATH_TO_JACOCO_OUTPUT_FOLDER,
					outputFile.getAbsolutePath());
		}
	}

	/** Integration test. */
	@Test(timeout = 60000)
	public void testIntegration() throws ConfigurationException, IOException {
		try {
			executeTestLogic();
			m_wasSuccessful = true;
		} catch (AssertionError ex) {
			m_wasSuccessful = false;
			throw ex;
		}
	}

	/** Execute the test logic. */
	protected abstract void executeTestLogic() throws ConfigurationException, IOException;

	/** After each test. */
	@After
	public void afterTest() {
		if (IntegrationTestConstants.DELETE_OUTPUT_AT_TEAR_DOWN_IF_SUCCESSFUL && m_wasSuccessful) {
			File file = new File(getSpecificFolderTestWorkingArea());

			if (file.exists()) {
				FileSystemUtils.deleteRecursively(file);
			}
		}
	}

	protected void executeTestAnalyzerWithConfiguration() throws ConfigurationException, ExecutionException {
		try {
			AnalyzerRunnerStart.execute(getConfiguration());
		} catch (IOException | ReflectiveOperationException e) {
			throw new ExecutionException(ExecutionIdFactory.ID_FOR_TESTS, e);
		}
	}

	protected void assertFilesExists(String errorMsg, File... files) {
		for (File file : files) {
			assertTrue(errorMsg + "(" + file.getPath() + ")", file.exists());
		}
	}

	protected void assertFileContentEqual(String errorMsg, boolean orderIsRelevant, File fileWithExpectedContent,
			File fileWithOutputContent) {
		Collection<String> expectedContent = getContent(fileWithExpectedContent);
		Collection<String> outputContent = getContent(fileWithOutputContent);

		if (!orderIsRelevant) {
			expectedContent = new HashSet<>(expectedContent);
			outputContent = new HashSet<>(outputContent);
		}

		assertEquals(errorMsg, expectedContent, outputContent);
	}

	protected void assertLogFileContains(List<String> expectedText) {
		if (expectedText.isEmpty()) {
			return;
		}

		List<String> logFileContentLines = getContent(getLogFile());
		String logFileContent = StringUtility.join(logFileContentLines, CommonConstants.NEW_LINE);

		for (String text : expectedText) {
			assertTrue("Log file does not contain: '" + text + "'", logFileContent.contains(text));
		}
	}

	protected Configuration getConfiguration() {
		return m_configuration;
	}

	protected String getSpecificFolderTestWorkingArea() {
		return TEST_WORKING_AREA + m_systemTestName + PATH_SEPARATOR;
	}

	protected String getSpecificFolderTestData() {
		return TEST_DATA_FOLDER + m_systemTestName + PATH_SEPARATOR;
	}

	protected String getSpecificFolderTestCoverage() {
		return TEST_COVERAGE_FOLDER + m_systemTestName + PATH_SEPARATOR;
	}

	protected String getCommonFolderTestData() {
		return TEST_DATA_COMMON_FOLDER;
	}

	protected String getFolderFromWorkingArea(String folder) {
		return TEST_WORKING_AREA_TO_ROOT + folder;
	}

	protected File getFileExpectedCollectedInformationAsText() {
		return getExpectedFile(getFileName(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION));
	}

	protected File getFileExpectedCollectedInformationAsSql() {
		return getExpectedFile(getFileName(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION
				.replace(FILE_EXTENSION_TXT, FILE_EXTENSION_SQL_TXT)));
	}

	protected File getFileExpectedResultAsText() {
		return getExpectedFile(getFileName(EnvironmentConstants.FILE_OUTPUT_RESULT_TXT));
	}

	protected File getFileExpectedResultAsSql() {
		return getExpectedFile(getFileName(EnvironmentConstants.FILE_OUTPUT_RESULT_SQL));
	}

	protected File getFileOutputCollectedInformation() {
		return getOutputFile(getFileName(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION));
	}

	protected File getFileOutputCollectedInformationAsSql() {
		return getOutputFile(getFileName(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION
				.replace(FILE_EXTENSION_TXT, FILE_EXTENSION_SQL_TXT)));
	}

	protected File getFileOutputResultAsText() {
		return getOutputFile(getFileName(EnvironmentConstants.FILE_OUTPUT_RESULT_TXT));
	}

	protected File getFileOutputResultAsSql() {
		return getOutputFile(getFileName(EnvironmentConstants.FILE_OUTPUT_RESULT_SQL));
	}

	protected File getFileOutputExecutionInformationAsSql() {
		return getOutputFile(getFileName(EnvironmentConstants.FILE_OUTPUT_EXECUTION_INFORMATION));
	}

	protected File getExpectedFile(String fileName) {
		return new File(getSpecificFolderTestData() + FOLDER_EXPECTED + fileName);
	}

	protected File getOutputFile(String fileName) {
		return new File(getSpecificFolderTestWorkingArea() + FOLDER_OUTPUT + fileName);
	}

	protected File getLogFile() {
		return new File(getSpecificFolderTestWorkingArea() + FOLDER_LOG + "TestAnalyzer.log");
	}

	protected File getFileInWorkingDirectory(String fileName) {
		return new File(getSpecificFolderTestWorkingArea() + fileName);
	}

	protected File getFileInSpecificTestDataFolder(String fileName) {
		return new File(getSpecificFolderTestData() + fileName);
	}

	protected List<String> getContent(File file) {
		try {
			return TextFileUtility.readFromFile(file.getPath());
		} catch (IOException ex) {
			fail("IOException when reading " + file.getAbsolutePath());
			return new LinkedList<>();
		}
	}

	protected String getFileName(String genericPath) {
		if (genericPath.contains(PATH_SEPARATOR)) {
			return genericPath.substring(genericPath.lastIndexOf(PATH_SEPARATOR) + PATH_SEPARATOR.length());
		} else {
			return genericPath;
		}
	}
}
