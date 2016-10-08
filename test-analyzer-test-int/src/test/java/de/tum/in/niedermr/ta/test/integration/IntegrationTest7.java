package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.MultiFileResultReceiver;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/**
 * Integration test with extensions involved.<br/>
 * <li>Code-statistics workflow</li>
 * <li>Stack-analysis workflow</li>
 * <li>Coverage-parser workflow</li>
 * <li>Return-type collector workflow</li>
 * <li>Test workflow for huge data</li>
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest7 extends AbstractIntegrationTest {
	private static final String ANALYSIS_INFORMATION_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "analysis-information" + FILE_EXTENSION_SQL_TXT;
	private static final String CODE_STATISTICS_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "code-statistics" + FILE_EXTENSION_SQL_TXT;
	private static final String COVERAGE_DATA_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "coverage-information" + FILE_EXTENSION_SQL_TXT;
	private static final String RETURN_TYPE_LIST_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "return-type-list" + FILE_EXTENSION_TXT;

	private File m_expectedStackAnalysisFile;
	private File m_outputStackAnalysisFile;
	private File m_expectedCodeStatisticsFile;
	private File m_outputCodeStatisticsFile;
	private File m_expectedReturnTypeListFile;
	private File m_outputReturnTypeListFile;
	private File m_expectedCollectedInformationFile;
	private File m_outputCollectedInformationFile;
	private File m_expectedResultFile;
	private File m_outputResultFile;
	private File m_expectedParsedCoverageFile;
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
		m_expectedStackAnalysisFile = getExpectedFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		m_outputStackAnalysisFile = getOutputFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		m_expectedCodeStatisticsFile = getExpectedFile(getFileName(CODE_STATISTICS_OUTPUT));
		m_outputCodeStatisticsFile = getOutputFile(getFileName(CODE_STATISTICS_OUTPUT));
		m_expectedReturnTypeListFile = getExpectedFile(getFileName(RETURN_TYPE_LIST_OUTPUT));
		m_outputReturnTypeListFile = getOutputFile(getFileName(RETURN_TYPE_LIST_OUTPUT));
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

		m_expectedParsedCoverageFile = getExpectedFile(getFileName(COVERAGE_DATA_OUTPUT));
		m_outputParsedCoverageFile = getOutputFile(getFileName(COVERAGE_DATA_OUTPUT));
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
