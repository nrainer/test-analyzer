package de.tum.in.niedermr.ta.test.integration;

public interface IntegrationTestConstants {
	public final static boolean DELETE_OUTPUT_AT_TEAR_DOWN_IF_SUCCESSFUL = true;

	public static final String MSG_NOT_EQUAL_RESULT = "Result not equal";
	public static final String MSG_NOT_EQUAL_COLLECTED_INFORMATION = "Collected information not equal";
	public static final String MSG_PATH_TO_TEST_JAR_IS_INCORRECT = "Path to test jar is incorrect";
	public static final String MSG_TEST_DATA_MISSING = "Test data missing";
	public static final String MSG_OUTPUT_MISSING = "Output file missing";

	public final static String JAR_TEST_DATA = "jars/test-project.jar";
	public final static String JAR_TESTNG_TESTS = "jars/test-project-testng-tests.jar";
}
