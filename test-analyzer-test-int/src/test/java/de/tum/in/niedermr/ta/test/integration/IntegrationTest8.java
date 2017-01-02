package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/**
 * Integration test with extensions involved.<br/>
 * <li>Stack-analysis workflow V2</li>
 * 
 * @see "configuration file in test data"
 */
@Ignore("This test is ignored because it runs only successfully if java.lang.Thread is replaced")
public class IntegrationTest8 extends AbstractIntegrationTest {
	private static final String ANALYSIS_INFORMATION_OUTPUT = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "analysis-information" + FILE_EXTENSION_SQL_TXT;

	private File m_expectedStackAnalysisFile;
	private File m_outputStackAnalysisFile;

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		initializeFiles();

		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFilesExists(MSG_TEST_DATA_MISSING, m_expectedStackAnalysisFile);

		executeTestAnalyzerWithConfiguration();

		checkResults();
	}

	private void initializeFiles() throws IOException {
		m_expectedStackAnalysisFile = getExpectedFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
		m_outputStackAnalysisFile = getOutputFile(getFileName(ANALYSIS_INFORMATION_OUTPUT));
	}

	private void checkResults() {
		assertFilesExists(MSG_OUTPUT_MISSING, m_outputStackAnalysisFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedStackAnalysisFile, m_outputStackAnalysisFile);
	}
}
