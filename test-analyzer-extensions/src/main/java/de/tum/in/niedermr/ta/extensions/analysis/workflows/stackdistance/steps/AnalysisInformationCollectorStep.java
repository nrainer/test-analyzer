package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.AbstractInformationCollectorStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.infocollection.IInformationCollectionLogic;

/** Analysis information collector step. */
public class AnalysisInformationCollectorStep extends AbstractInformationCollectorStep {

	/** Information collection logic class. */
	private Class<? extends IInformationCollectionLogic> m_informationCollectionLogicClass;

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
		return getFileInWorkingArea(ExtensionEnvironmentConstants.FILE_OUTPUT_ANALYSIS_INFORMATION);
	}

	/** {@inheritDoc} */
	@Override
	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(
				getFileInWorkingArea(ExtensionEnvironmentConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), 0,
				configuration.getCodePathToMutate().countElements());
	}

	/** {@link #m_informationCollectionLogicClass} */
	public void setInformationCollectorLogicClass(
			Class<? extends IInformationCollectionLogic> informationCollectionLogicClass) {
		m_informationCollectionLogicClass = informationCollectionLogicClass;
	}

	/** {@inheritDoc} */
	@Override
	protected Class<? extends IInformationCollectionLogic> getInformationCollectorLogicClass() {
		return m_informationCollectionLogicClass;
	}
}
