package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/**
 * Integration test with extensions involved.<br/>
 * <li>Code-statistics workflow</li>
 * <li>Stack-analysis workflow</li>
 * <li>Coverage-parser workflow</li>
 * <li>Return-type collector workflow</li>
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

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		File expectedStackAnalysisFile = getExpectedFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		File outputStackAnalysisFile = getOutputFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		File expectedCodeStatisticsFile = getExpectedFile(getFileName(CODE_STATISTICS_OUTPUT));
		File outputCodeStatisticsFile = getOutputFile(getFileName(CODE_STATISTICS_OUTPUT));
		File expectedReturnTypeListFile = getExpectedFile(getFileName(RETURN_TYPE_LIST_OUTPUT));
		File outputReturnTypeListFile = getOutputFile(getFileName(RETURN_TYPE_LIST_OUTPUT));

		File inputCoverageXmlFile = getFileInSpecificTestDataFolder("other/coverage.xml");
		File inputCoverageXmlFileInWorkingDirectory = getFileInWorkingDirectory("coverage.xml");
		assertFilesExists(MSG_TEST_DATA_MISSING, inputCoverageXmlFile);
		Files.copy(inputCoverageXmlFile.toPath(), inputCoverageXmlFileInWorkingDirectory.toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		assertFilesExists(MSG_TEST_DATA_MISSING, inputCoverageXmlFileInWorkingDirectory);

		File expectedParsedCoverageFile = getExpectedFile(getFileName(COVERAGE_DATA_OUTPUT));
		File outputParsedCoverageFile = getOutputFile(getFileName(COVERAGE_DATA_OUTPUT));

		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFilesExists(MSG_TEST_DATA_MISSING, expectedStackAnalysisFile, expectedCodeStatisticsFile,
				expectedReturnTypeListFile);

		executeTestAnalyzerWithConfiguration();

		assertFilesExists(MSG_OUTPUT_MISSING, outputStackAnalysisFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, expectedStackAnalysisFile, outputStackAnalysisFile);

		assertFilesExists(MSG_OUTPUT_MISSING, outputCodeStatisticsFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, expectedCodeStatisticsFile, outputCodeStatisticsFile);

		assertFilesExists(MSG_OUTPUT_MISSING, outputParsedCoverageFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, expectedParsedCoverageFile, outputParsedCoverageFile);

		assertFilesExists(MSG_OUTPUT_MISSING, outputReturnTypeListFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, expectedReturnTypeListFile, outputReturnTypeListFile);
	}
}
