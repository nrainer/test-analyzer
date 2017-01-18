package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/** Further constants needed in the extensions. */
public interface ExtensionEnvironmentConstants extends EnvironmentConstants {
	String FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X = PATH_WORKING_AREA_TEMP + "analysis_instr_src_%s"
			+ FILE_EXTENSION_JAR;
	String FILE_OUTPUT_ANALYSIS_INFORMATION = PATH_WORKING_AREA_RESULT + "stack-distances" + FILE_EXTENSION_SQL_TXT;
}
