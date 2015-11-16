package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Jar: simple-project-special.jar Multi-threaded: yes (4) Comments: -
 */
public class IntegrationTest5 extends AbstractSystemTest {
	@Override
	public void testSystemInternal() throws ConfigurationException, IOException {
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_SPECIAL));
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformation());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedResult());

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputResult());

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, true, getFileExpectedCollectedInformation(), getFileOutputCollectedInformation());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResult(), getFileOutputResult());
	}
}
