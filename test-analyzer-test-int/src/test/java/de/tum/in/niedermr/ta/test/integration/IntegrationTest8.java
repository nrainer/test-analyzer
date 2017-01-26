package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test with extensions involved.<br/>
 * <li>Stack-analysis workflow V2</li>
 * 
 * @see "configuration file in test data"
 */
@Ignore("This test is ignored because it runs only successfully if java.lang.Thread is replaced")
public class IntegrationTest8 extends AbstractIntegrationTest {
	private static final String STACK_DISTANCES_V2_OUTPUT = ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V2;

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
		m_expectedStackAnalysisFile = getExpectedFile(getFileName(STACK_DISTANCES_V2_OUTPUT));
		m_outputStackAnalysisFile = getOutputFile(getFileName(STACK_DISTANCES_V2_OUTPUT));
	}

	private void checkResults() {
		assertFilesExists(MSG_OUTPUT_MISSING, m_outputStackAnalysisFile);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedStackAnalysisFile, m_outputStackAnalysisFile);
	}
}
