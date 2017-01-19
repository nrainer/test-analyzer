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
		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFilesExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsText());
		assertFilesExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsSql());

		executeTestAnalyzerWithConfiguration();

		assertFilesExists(MSG_OUTPUT_MISSING, getFileOutputResultAsSql());

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, false, getFileExpectedCollectedInformationAsText(),
				getFileOutputCollectedInformation());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResultAsSql(), getFileOutputResultAsSql());

		checkLogFile();
	}

	/** Check the log file. */
	protected void checkLogFile() {
		List<String> expectedLogFileTextChunks = new ArrayList<>();
		expectedLogFileTextChunks
				.add(TestcaseIdentifier.create("de.tum.in.ma.simpleproject.special.HasFailingTest", "failingTest").get()
						+ " will be skipped!");
		expectedLogFileTextChunks.add(
				"Test execution did not complete: de.tum.in.ma.simpleproject.special.Special.returnFiveForTestNotToExit()");
		expectedLogFileTextChunks.add(
				"ALL THREADS FINISHED. 14 methods. 10 processed successfully. 2 ignored. 1 with timeout. 1 failed.");
		// Lambda mutation is not supported
		expectedLogFileTextChunks.add("Not mutated: de.tum.in.ma.simpleproject.special.Java8.lambda$1");
		assertLogFileContains(expectedLogFileTextChunks);
	}
}
