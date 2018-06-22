package de.tum.in.niedermr.ta.core.common;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;

public class TestUtility {
	private static final String TEST_FOLDER = "src/test/data/";

	public static String getTestFolder(Class<?> testCase) {
		return TEST_FOLDER + testCase.getSimpleName() + "/";
	}

	public static String loadTestContent(Class<?> testClass, String inputFileName) throws IOException {
		List<String> lines = TextFileUtility.readFromFile(getTestFolder(testClass) + inputFileName);
		return loadTestContent(lines);
	}

	public static String loadTestContent(InMemoryResultReceiver resultReceiver) throws IOException {
		return loadTestContent(resultReceiver.getResult());
	}

	private static String loadTestContent(List<String> lines) throws IOException {
		String content = StringUtility.join(lines, CommonConstants.NEW_LINE);
		content = TestUtility.alignLineEndings(content);
		return content;
	}

	public static String alignLineEndings(String s) {
		return s.replace("\r\n", "\n");
	}

	public static void assertFileContentMatchesResultReceiver(Class<?> testClass, String fileName,
			InMemoryResultReceiver resultReceiver) throws IOException {
		String expectedResult = TestUtility.loadTestContent(testClass, fileName);
		String actualResult = TestUtility.loadTestContent(resultReceiver);
		assertEquals(expectedResult, actualResult);
	}
}
