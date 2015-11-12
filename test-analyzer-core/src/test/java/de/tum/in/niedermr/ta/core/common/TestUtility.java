package de.tum.in.niedermr.ta.core.common;

public class TestUtility {
	private static final String TEST_FOLDER = "src/test/data/";

	public static String getTestFolder(Class<?> testCase) {
		return TEST_FOLDER + testCase.getSimpleName() + "/";
	}
}
