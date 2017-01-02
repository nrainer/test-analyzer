package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisConstants;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.AbstractInformationCollectorStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;

/** Analysis information collector step. */
public abstract class AbstractAnalysisInformationCollectorStep extends AbstractInformationCollectorStep {

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "ANACOL";
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Analyzing stack distance";
	}

	/** {@inheritDoc} */
	@Override
	protected String getFileWithResultsParameterValue() {
		return getFileInWorkingArea(AnalysisConstants.FILE_OUTPUT_ANALYSIS_INFORMATION);
	}

	/** {@inheritDoc} */
	@Override
	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(
				getFileInWorkingArea(AnalysisConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), 0,
				configuration.getCodePathToMutate().countElements());
	}
}
