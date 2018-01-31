package de.tum.in.niedermr.ta.extensions.analysis.workflows;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/** Further constants needed in the extensions. */
public interface ExtensionEnvironmentConstants extends EnvironmentConstants {
	/** Instrumented temporary jar file name for analyses. */
	String FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X = PATH_WORKING_AREA_TEMP + "analysis_instr_src_%s"
			+ FILE_EXTENSION_JAR;

	/** Output file for the computed stack distances (workflow V1). */
	String FILE_OUTPUT_STACK_DISTANCES_V1 = PATH_WORKING_AREA_RESULT + "stack-distances-v1" + FILE_EXTENSION_SQL_TXT;

	/** Output file for the computed stack distances (workflow V2). */
	String FILE_OUTPUT_STACK_DISTANCES_V2 = PATH_WORKING_AREA_RESULT + "stack-distances-v2" + FILE_EXTENSION_SQL_TXT;

	/** Output file for the computed stack distances (workflow V3). */
	String FILE_OUTPUT_STACK_DISTANCES_V3 = PATH_WORKING_AREA_RESULT + "stack-distances-v3" + FILE_EXTENSION_SQL_TXT;

	/** Output file for the computed code statistics. */
	String FILE_OUTPUT_CODE_STATISTICS = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "code-statistics"
			+ FileSystemConstants.FILE_EXTENSION_SQL_TXT;

	/** Output file for the parsed coverage information. */
	String FILE_OUTPUT_COVERAGE_INFORMATION = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "coverage-information"
			+ FileSystemConstants.FILE_EXTENSION_SQL_TXT;

	/** Output file for the line level coverage. */
	String FILE_OUTPUT_LINE_LEVEL_COVERAGE = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "line-level-coverage"
			+ FileSystemConstants.FILE_EXTENSION_SQL_TXT;

	/** Output file for the retrieved method return types. */
	String FILE_OUTPUT_METHOD_RETURN_TYPES = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "return-type-list"
			+ FileSystemConstants.FILE_EXTENSION_TXT;

	/** Output file for the converter PIT data. */
	String FILE_OUTPUT_PIT_DATA = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "pit-data"
			+ FileSystemConstants.FILE_EXTENSION_SQL_TXT;
}
