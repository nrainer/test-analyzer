package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/**
 * Integration test with extensions involved.<br/>
 * Code-statistics workflow. Stack-analysis workflow.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest7 extends AbstractIntegrationTest {
	private static final String ANALYSIS_INFORMATION_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "analysis-information" + FILE_EXTENSION_SQL_TXT;
	private static final String CODE_STATISTICS_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "code-statistics" + FILE_EXTENSION_SQL_TXT;

	@Override
	public void testSystemInternal() throws ConfigurationException, IOException {
		File expectedStackAnalysisFile = getExpectedFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		File outputStackAnalysisFile = getOutputFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		File expectedCodeStatisticsFile = getExpectedFile(getFileName(CODE_STATISTICS_OUTPUT));
		File outputCodeStatisticsFile = getOutputFile(getFileName(CODE_STATISTICS_OUTPUT));

		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFileExists(MSG_TEST_DATA_MISSING, expectedStackAnalysisFile);
		assertFileExists(MSG_TEST_DATA_MISSING, expectedCodeStatisticsFile);

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, outputStackAnalysisFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, expectedStackAnalysisFile, outputStackAnalysisFile);

		assertFileExists(MSG_OUTPUT_MISSING, outputCodeStatisticsFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, expectedCodeStatisticsFile, outputCodeStatisticsFile);
	}
}
