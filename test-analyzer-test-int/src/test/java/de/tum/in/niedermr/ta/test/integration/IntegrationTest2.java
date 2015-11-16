package de.tum.in.niedermr.ta.test.integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.infocollection.CollectedInformation;

/**
 * Jar: simple-project-core.jar Multi-threaded: no Comments: skipping one test class, skipping setters and getters
 */
public class IntegrationTest2 extends AbstractSystemTest {
	@Override
	public void testSystemInternal() throws ConfigurationException, IOException {
		assertFileExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, new File(getCommonFolderTestData() + JAR_CORE));
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformation());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedCollectedInformationAsSql());
		assertFileExists(MSG_TEST_DATA_MISSING, getFileExpectedResult());

		executeTestAnalyzerWithConfiguration();

		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputCollectedInformation());
		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputCollectedInformationAsSql());
		assertFileExists(MSG_OUTPUT_MISSING, getFileOutputResult());

		List<TestInformation> expectedTestInformationList = CollectedInformation
				.parseInformationCollectorData(getContent(getFileExpectedCollectedInformation()));
		List<TestInformation> outputTestInformationList = CollectedInformation.parseInformationCollectorData(getContent(getFileOutputCollectedInformation()));
		Set<TestInformation> expectedTestInformationSet = new HashSet<>(expectedTestInformationList);
		Set<TestInformation> outputTestInformationSet = new HashSet<>(outputTestInformationList);
		assertEquals(MSG_NOT_EQUAL_COLLECTED_INFORMATION, expectedTestInformationSet, outputTestInformationSet);

		assertFileContentEqual(MSG_NOT_EQUAL_COLLECTED_INFORMATION, false, getFileExpectedCollectedInformationAsSql(), getFileOutputCollectedInformationAsSql());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, false, getFileExpectedResult(), getFileOutputResult());
	}
}