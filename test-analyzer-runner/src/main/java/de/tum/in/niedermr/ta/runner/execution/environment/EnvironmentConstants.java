package de.tum.in.niedermr.ta.runner.execution.environment;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public interface EnvironmentConstants extends FileSystemConstants {
	/**
	 * FOLDERS
	 */
	public static final String FOLDER_PROGRAM = "{ROOT}/";
	public static final String FOLDER_WORKING_AREA = "{WORKING_FOLDER}/";

	/**
	 * PATHS
	 */
	public static final String PATH_WORKING_AREA_TEMP = FOLDER_WORKING_AREA + "temp/";
	public static final String PATH_WORKING_AREA_RESULT = FOLDER_WORKING_AREA + "result/";

	/**
	 * FILES
	 */
	public static final String FILE_INPUT_USED_CONFIG = FOLDER_WORKING_AREA + "used-config" + FILE_EXTENSION_CONFIG;
	public static final String FILE_TEMP_JAR_X = PATH_WORKING_AREA_TEMP + "temp_%s" + FILE_EXTENSION_JAR;
	public static final String FILE_TEMP_JAR_INSTRUMENTED_SOURCE_X = PATH_WORKING_AREA_TEMP + "instrumented_src_%s"
			+ FILE_EXTENSION_JAR;
	public static final String FILE_TEMP_JAR_INSTRUMENTED_TEST_X = PATH_WORKING_AREA_TEMP + "instrumented_tst_%s"
			+ FILE_EXTENSION_JAR;
	public static final String FILE_TEMP_TESTS_TO_RUN_X = PATH_WORKING_AREA_TEMP + "tests-to-run_%s"
			+ FILE_EXTENSION_TXT;
	public static final String FILE_TEMP_RESULT_X = PATH_WORKING_AREA_TEMP + "result_%s" + FILE_EXTENSION_TXT;
	public static final String FILE_TEMP_IS_RUNNING_TESTS = PATH_WORKING_AREA_TEMP + "is-running-tests"
			+ FILE_EXTENSION_TXT;
	public static final String FILE_OUTPUT_COLLECTED_INFORMATION = PATH_WORKING_AREA_RESULT + "collected-information"
			+ FILE_EXTENSION_TXT;
	public static final String FILE_OUTPUT_RESULT_NO_ENDING = PATH_WORKING_AREA_RESULT + "result";
	public static final String FILE_OUTPUT_RESULT_TXT = PATH_WORKING_AREA_RESULT + "result" + FILE_EXTENSION_TXT;
	public static final String FILE_OUTPUT_RESULT_SQL = PATH_WORKING_AREA_RESULT + "result" + FILE_EXTENSION_SQL_TXT;
}
