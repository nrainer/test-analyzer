package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

public interface AnalysisConstants extends EnvironmentConstants {
	String FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X = PATH_WORKING_AREA_TEMP + "analysis_instr_src_%s" + FILE_EXTENSION_JAR;
	String FILE_OUTPUT_ANALYSIS_INFORMATION = PATH_WORKING_AREA_RESULT + "analysis-information" + FILE_EXTENSION_SQL_TXT;
}
