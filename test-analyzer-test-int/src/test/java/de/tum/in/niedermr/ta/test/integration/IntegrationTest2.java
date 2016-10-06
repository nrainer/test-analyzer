package de.tum.in.niedermr.ta.test.integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.infocollection.CollectedInformationUtility;

/**
 * Integration test.<br/>
 * Nothing special.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest2 extends AbstractIntegrationTest {

	@Override
	public void testSystemInternal() throws ConfigurationException, IOException {
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_TEST_DATA));
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsText());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsSql());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsText());

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputCollectedInformation());
		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputCollectedInformationAsSql());
		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputResultAsText());

		List<TestInformation> expectedTestInformationList = CollectedInformationUtility
				.parseMethodTestcaseText(getContent(getFileExpectedCollectedInformationAsText()));
		List<TestInformation> outputTestInformationList = CollectedInformationUtility
				.parseMethodTestcaseText(getContent(getFileOutputCollectedInformation()));
		Set<TestInformation> expectedTestInformationSet = new HashSet<>(expectedTestInformationList);
		Set<TestInformation> outputTestInformationSet = new HashSet<>(outputTestInformationList);
		assertEquals(MSG_NOT_EQUAL_COLLECTED_INFORMATION, expectedTestInformationSet, outputTestInformationSet);

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, false, getFileExpectedCollectedInformationAsSql(),
				getFileOutputCollectedInformationAsSql());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResultAsText(), getFileOutputResultAsText());
	}
}
