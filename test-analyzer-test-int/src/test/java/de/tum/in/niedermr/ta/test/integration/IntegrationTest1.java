package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Jar: simple-project-lite.jar Multi-threaded: no Comments: -
 */
public class IntegrationTest1 extends AbstractSystemTest {
	@Override
	public void testSystemInternal() throws ConfigurationException, IOException {
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_LITE));
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformation());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedResult());

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputCollectedInformation());
		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputResult());

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, true, getFileExpectedCollectedInformation(), getFileOutputCollectedInformation());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, true, getFileExpectedResult(), getFileOutputResult());
	}
}
