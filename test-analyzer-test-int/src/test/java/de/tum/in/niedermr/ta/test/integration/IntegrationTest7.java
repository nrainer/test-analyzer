package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.MultiFileResultReceiver;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test with extensions involved.<br/>
 * <li>Code-statistics workflow</li>
 * <li>Stack-analysis workflow V1</li>
 * <li>Coverage-parser workflow</li>
 * <li>Return-type collector workflow</li>
 * <li>Test workflow for huge data</li>
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest7 extends AbstractIntegrationTest {
	/** Expected file. */
	private File m_expectedStackAnalysisFile;
	/** Expected file. */
	private File m_expectedCodeStatisticsFile;
	/** Expected file. */
	private File m_expectedReturnTypeListFile;
	/** Expected file. */
	private File m_expectedCollectedInformationFile;
	/** Expected file. */
	private File m_expectedResultFile;
	/** Expected file. */
	private File m_expectedParsedCoverageFile;

	/** Actual output file. */
	private File m_outputStackAnalysisFile;
	/** Actual output file. */
	private File m_outputCodeStatisticsFile;
	/** Actual output file. */
	private File m_outputReturnTypeListFile;
	/** Actual output file. */
	private File m_outputCollectedInformationFile;
	/** Actual output file. */
	private File m_outputResultFile;
	/** Actual output file. */
	private File m_outputParsedCoverageFile;

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		initializeFiles();

		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFilesExists(MSG_TEST_DATA_MISSING, m_expectedStackAnalysisFile, m_expectedCodeStatisticsFile,
				m_expectedReturnTypeListFile, m_expectedCollectedInformationFile, m_expectedResultFile,
				m_expectedParsedCoverageFile);

		executeTestAnalyzerWithConfiguration();

		checkResults();
	}

	private void initializeFiles() throws IOException {
		m_expectedStackAnalysisFile = getExpectedFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V1));
		m_outputStackAnalysisFile = getOutputFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V1));
		m_expectedCodeStatisticsFile = getExpectedFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_CODE_STATISTICS));
		m_outputCodeStatisticsFile = getOutputFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_CODE_STATISTICS));
		m_expectedReturnTypeListFile = getExpectedFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_METHOD_RETURN_TYPES));
		m_outputReturnTypeListFile = getOutputFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_METHOD_RETURN_TYPES));
		m_expectedCollectedInformationFile = new File(MultiFileResultReceiver.getFileName(
				getFileExpectedCollectedInformationAsSql().getPath(), MultiFileResultReceiver.FIRST_INDEX));
		m_outputCollectedInformationFile = new File(MultiFileResultReceiver
				.getFileName(getFileOutputCollectedInformationAsSql().getPath(), MultiFileResultReceiver.FIRST_INDEX));
		m_expectedResultFile = new File(MultiFileResultReceiver.getFileName(getFileExpectedResultAsSql().getPath(),
				MultiFileResultReceiver.FIRST_INDEX));
		m_outputResultFile = new File(MultiFileResultReceiver.getFileName(getFileOutputResultAsSql().getPath(),
				MultiFileResultReceiver.FIRST_INDEX));

		File inputCoverageXmlFile = getFileInSpecificTestDataFolder("other/coverage.xml");
		File inputCoverageXmlFileInWorkingDirectory = getFileInWorkingDirectory("coverage.xml");
		assertFilesExists(MSG_TEST_DATA_MISSING, inputCoverageXmlFile);
		Files.copy(inputCoverageXmlFile.toPath(), inputCoverageXmlFileInWorkingDirectory.toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		assertFilesExists(MSG_TEST_DATA_MISSING, inputCoverageXmlFileInWorkingDirectory);

		m_expectedParsedCoverageFile = getExpectedFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_COVERAGE_INFORMATION));
		m_outputParsedCoverageFile = getOutputFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_COVERAGE_INFORMATION));
	}

	private void checkResults() {
		assertFilesExists(MSG_OUTPUT_MISSING, m_outputStackAnalysisFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedStackAnalysisFile, m_outputStackAnalysisFile);

		assertFilesExists(MSG_OUTPUT_MISSING, m_outputCodeStatisticsFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedCodeStatisticsFile, m_outputCodeStatisticsFile);

		assertFilesExists(MSG_OUTPUT_MISSING, m_outputParsedCoverageFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedParsedCoverageFile, m_outputParsedCoverageFile);

		assertFilesExists(MSG_OUTPUT_MISSING, m_outputReturnTypeListFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedReturnTypeListFile, m_outputReturnTypeListFile);

		assertFilesExists(MSG_OUTPUT_MISSING, m_outputCollectedInformationFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedCollectedInformationFile,
				m_outputCollectedInformationFile);

		assertFilesExists(MSG_OUTPUT_MISSING, m_outputResultFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedResultFile, m_outputResultFile);
	}
}
