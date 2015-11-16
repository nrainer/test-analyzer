package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/**
 * Jar: simple-project-core.jar Multi-threaded: no Comments: stack analysis workflow
 */
public class IntegrationTest7 extends AbstractSystemTest {
	private static final String ANALYSIS_INFORMATION_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "analysis-information" + FILE_EXTENSION_SQL_TXT;

	@Override
	public void testSystemInternal() throws ConfigurationException, IOException {
		File expectedStackAnalysisFile = getExpectedFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		File outputStackAnalysisFile = getOutputFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));

		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_CORE));
		assertFileExists(MSG_TEST_DATA_MISSING, expectedStackAnalysisFile);

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, outputStackAnalysisFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, expectedStackAnalysisFile, outputStackAnalysisFile);
	}
}
