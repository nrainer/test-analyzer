package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test.<br/>
 * Multi-threaded.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest4 extends AbstractIntegrationTest {

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFilesExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsText());
		assertFilesExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsSql());

		executeTestAnalyzerWithConfiguration();

		assertFilesExists(MSG_OUTPUT_MISSING, getFileOutputResultAsSql());

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, false, getFileExpectedCollectedInformationAsText(),
				getFileOutputCollectedInformation());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResultAsSql(), getFileOutputResultAsSql());
	}
}
