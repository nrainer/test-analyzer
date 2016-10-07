package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test.<br/>
 * Multi-threaded. Uses testNG.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest6 extends AbstractIntegrationTest implements FileSystemConstants {

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TESTNG_TESTS));
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsText());

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputResultAsText());

		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResultAsText(), getFileOutputResultAsText());
	}
}
