package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test.<br/>
 * Multi-threaded. Mutation with special code. Log file checks.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest5 extends AbstractIntegrationTest {

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsText());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsSql());

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputResultAsSql());

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, false, getFileExpectedCollectedInformationAsText(),
				getFileOutputCollectedInformation());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResultAsSql(), getFileOutputResultAsSql());

		List<String> expectedLogFileTextChunks = new ArrayList<>();
		expectedLogFileTextChunks
				.add(TestcaseIdentifier.create("de.tum.in.ma.simpleproject.special.HasFailingTest", "failingTest").get()
						+ " will be skipped!");
		// Lambda mutation is not supported
		expectedLogFileTextChunks.add("Skipped: de.tum.in.ma.simpleproject.special.Java8.lambda$1");
		assertLogFileContains(expectedLogFileTextChunks);
	}
}
