package de.tum.in.niedermr.ta.test.integration;

/** Constants for integration tests. */
public interface IntegrationTestConstants {
	public static final boolean DELETE_OUTPUT_AT_TEAR_DOWN_IF_SUCCESSFUL = true;

	/** Whether JaCoCo should be used if it is configured in the configuration. */
	public static final boolean USE_JACOCO_IF_CONFIGURED = true;

	public static final String MSG_NOT_EQUAL_RESULT = "Result not equal";
	public static final String MSG_NOT_EQUAL_COLLECTED_INFORMATION = "Collected information not equal";
	public static final String MSG_PATH_TO_TEST_JAR_IS_INCORRECT = "Path to test jar is incorrect";
	public static final String MSG_TEST_DATA_MISSING = "Test data missing";
	public static final String MSG_OUTPUT_MISSING = "Output file missing";

	public static final String JAR_TEST_DATA = "jars/test-project.jar";
	public static final String JAR_TESTNG_TESTS = "jars/test-project-testng-tests.jar";
}
