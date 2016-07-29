package de.tum.in.niedermr.ta.test.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test.<br/>
 * Multi-threaded.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest3 extends AbstractIntegrationTest {
	@Override
	public void testSystemInternal() throws ConfigurationException, IOException {
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsText());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsSql());

		FileSystemUtils.copyFile(getFileExpectedCollectedInformationAsText(), getFileOutputCollectedInformation());

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputResultAsSql());
		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputExecutionInformationAsSql());

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, false, getFileExpectedCollectedInformationAsText(),
				getFileOutputCollectedInformation());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResultAsSql(), getFileOutputResultAsSql());

		String executionInformationContent = StringUtility.join(getContent(getFileOutputExecutionInformationAsSql()),
				CommonConstants.NEW_LINE);
		assertTrue(executionInformationContent.contains(
				"INSERT INTO Execution_Information (execution, date, project, configurationContent) VALUES ('TEST', CURRENT_DATE(), '?', '"));
		assertTrue(executionInformationContent.contains(
				"UPDATE Execution_Information SET notes = '12 methods. 6 processed successfully. 6 skipped. 0 with timeout. 0 failed.' WHERE execution = 'TEST';"));
	}
}
