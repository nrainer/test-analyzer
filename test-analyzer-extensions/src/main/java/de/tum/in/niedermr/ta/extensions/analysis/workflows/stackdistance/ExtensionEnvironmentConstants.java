package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

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
}
