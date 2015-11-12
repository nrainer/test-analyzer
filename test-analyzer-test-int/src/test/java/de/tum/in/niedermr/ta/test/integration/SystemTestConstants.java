package de.tum.in.niedermr.ta.test.integration;

public interface SystemTestConstants {
	public final static boolean DELETE_OUTPUT_AT_TEAR_DOWN_IF_SUCCESSFUL = true;

	public static final String MSG_NOT_EQUAL_RESULT = "Result not equal";
	public static final String MSG_NOT_EQUAL_COLLECTED_INFORMATION = "Collected information not equal";
	public static final String MSG_PATH_TO_TEST_JAR_IS_INCORRECT = "Path to test jar is incorrect";
	public static final String MSG_PATH_TO_DEPENDENCY_IS_INCORRECT = "Path to dependency is incorrect";
	public static final String MSG_TEST_DATA_MISSING = "Test data missing";
	public static final String MSG_OUTPUT_MISSING = "Output file missing";

	public final static String JAR_LITE = "jars/simple-project-lite.jar";
	public final static String JAR_CORE = "jars/simple-project-core.jar";
	public final static String JAR_SYSTEM = "jars/simple-project-system.jar";
	public final static String JAR_SPECIAL = "jars/simple-project-special.jar";
	public final static String JAR_TESTNG = "jars/simple-project-testng.jar";
}
