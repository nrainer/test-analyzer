package de.tum.in.niedermr.ta.runner.execution.environment;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public interface EnvironmentConstants extends FileSystemConstants {
	/**
	 * SETTINGS
	 */
	static final boolean USE_BINS_INSTEAD_OF_JARS = true;
	static final boolean USE_ALL_IN_ONE_JAR = false;

	/**
	 * FOLDERS
	 */
	public static final String FOLDER_PROGRAM = "{ROOT}/";
	public static final String FOLDER_WORKING_AREA = "{WORKING_FOLDER}/";

	/**
	 * PATHS
	 */
	static final String PATH_PROGRAM_JARS = FOLDER_PROGRAM + "jar/";
	static final String PATH_PROGRAM_EXTERNAL_JARS = FOLDER_PROGRAM + "../Core/lib/";
	public static final String PATH_WORKING_AREA_TEMP = FOLDER_WORKING_AREA + "temp/";
	public static final String PATH_WORKING_AREA_RESULT = FOLDER_WORKING_AREA + "result/";

	/**
	 * INTERNAL BINARIES
	 */
	static final String BIN_TEST_ANALYZER_RUNNER = FOLDER_PROGRAM + "bin/" + CP_SEP;
	static final String BIN_CORE = FOLDER_PROGRAM + "../Core/bin/" + CP_SEP;

	/**
	 * INTERNAL JAR FILES
	 */
	static final String JAR_TEST_ANALYZER_RUNNER = PATH_PROGRAM_JARS + "test-analyzer-runner.jar" + CP_SEP;
	static final String JAR_CORE = PATH_PROGRAM_JARS + "test-analyzer-core.jar" + CP_SEP;
	static final String JAR_ALL_IN_ONE = PATH_PROGRAM_JARS + "test-analyzer-all.jar" + CP_SEP;

	/**
	 * REFERENCED INTERNAL CODE
	 */
	public static final String CODE_CORE = USE_BINS_INSTEAD_OF_JARS ? BIN_CORE
			: (USE_ALL_IN_ONE_JAR ? JAR_ALL_IN_ONE : JAR_CORE);
	public static final String CODE_TEST_ANALYZER_START = USE_BINS_INSTEAD_OF_JARS ? BIN_TEST_ANALYZER_RUNNER
			: (USE_ALL_IN_ONE_JAR ? JAR_ALL_IN_ONE : JAR_TEST_ANALYZER_RUNNER);

	/**
	 * EXTERNAL JAR FILES
	 */
	public static final String EXTERNAL_JARS_ASM = PATH_PROGRAM_EXTERNAL_JARS + "asm-5.0.3.jar" + CP_SEP
			+ PATH_PROGRAM_EXTERNAL_JARS + "asm-commons-5.0.3.jar" + CP_SEP + PATH_PROGRAM_EXTERNAL_JARS
			+ "asm-tree-5.0.3.jar" + CP_SEP;
	public static final String EXTERNAL_JARS_LOG4J = PATH_PROGRAM_EXTERNAL_JARS + "log4j-api-2.0-beta4.jar" + CP_SEP
			+ PATH_PROGRAM_EXTERNAL_JARS + "log4j-core-2.0-beta4.jar" + CP_SEP;
	public static final String EXTERNAL_JARS_JUNIT = PATH_PROGRAM_EXTERNAL_JARS + "junit_4.8.2.jar" + CP_SEP
			+ PATH_PROGRAM_EXTERNAL_JARS + "hamcrest_1.1.0.jar" + CP_SEP;
	public static final String EXTERNAL_JARS_COMMONS_IO = PATH_PROGRAM_EXTERNAL_JARS + "commons-io-2.4.jar" + CP_SEP;
	public static final String EXTERNAL_JARS_CCSM_COMMONS = PATH_PROGRAM_EXTERNAL_JARS + "ccsm-commons.jar" + CP_SEP;

	/**
	 * JAR BUNDLES
	 */
	public static final String JAR_BUNDLE_ASM_COMMONSIO_LOG4J_CORE = EXTERNAL_JARS_ASM + EXTERNAL_JARS_COMMONS_IO
			+ EXTERNAL_JARS_LOG4J + CODE_CORE;
	public static final String JAR_BUNDLE_ASM_COMMONSIO_LOG4J_CORE_JUNIT = JAR_BUNDLE_ASM_COMMONSIO_LOG4J_CORE
			+ EXTERNAL_JARS_JUNIT;

	/**
	 * CLASSPATHS
	 */
	public static final String CLASSPATH_TEST_ANALYZER = JAR_BUNDLE_ASM_COMMONSIO_LOG4J_CORE_JUNIT
			+ CODE_TEST_ANALYZER_START + EXTERNAL_JARS_CCSM_COMMONS;

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
