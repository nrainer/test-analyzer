package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test.<br/>
 * Nothing special.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest1 extends AbstractIntegrationTest {

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFilesExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsText());
		assertFilesExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsText());

		executeTestAnalyzerWithConfiguration();

		assertFilesExists(MSG_OUTPUT_MISSING, getFileOutputCollectedInformation());
		assertFilesExists(MSG_OUTPUT_MISSING, getFileOutputResultAsText());

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, true, getFileExpectedCollectedInformationAsText(),
				getFileOutputCollectedInformation());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, true, getFileExpectedResultAsText(), getFileOutputResultAsText());
	}
}
