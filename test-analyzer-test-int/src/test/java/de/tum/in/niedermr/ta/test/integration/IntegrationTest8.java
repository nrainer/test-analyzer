package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test with extensions involved.<br/>
 * <li>Stack-analysis workflow V2</li>
 * <li>Stack-analysis workflow V3</li>
 * 
 * @see "configuration file in test data"
 */
@Ignore("This test is ignored because it runs only successfully if java.lang.Thread is replaced")
public class IntegrationTest8 extends AbstractIntegrationTest {

	/** Expected stack distance V2 file. */
	private File m_expectedStackDistanceV2File;
	/** Expected stack distance V3 file. */
	private File m_expectedStackDistanceV3File;
	/** Actual stack distance V2 file. */
	private File m_outputStackDistanceV2File;
	/** Actual stack distance V3 file. */
	private File m_outputStackDistanceV3File;

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		initializeFiles();

		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFilesExists(MSG_TEST_DATA_MISSING, m_expectedStackDistanceV2File);
		assertFilesExists(MSG_TEST_DATA_MISSING, m_expectedStackDistanceV3File);

		executeTestAnalyzerWithConfiguration();

		checkResults();
	}

	/** Initialize the expected and actual result files. */
	private void initializeFiles() throws IOException {
		m_expectedStackDistanceV2File = getExpectedFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V2));
		m_outputStackDistanceV2File = getOutputFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V2));
		m_expectedStackDistanceV3File = getExpectedFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V3));
		m_outputStackDistanceV3File = getOutputFile(
				getFileName(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V3));
	}

	/** Check the results. */
	private void checkResults() {
		assertFilesExists(MSG_OUTPUT_MISSING, m_outputStackDistanceV2File);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedStackDistanceV2File, m_outputStackDistanceV2File);

		assertFilesExists(MSG_OUTPUT_MISSING, m_outputStackDistanceV3File);
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, m_expectedStackDistanceV3File, m_outputStackDistanceV3File);
	}
}
