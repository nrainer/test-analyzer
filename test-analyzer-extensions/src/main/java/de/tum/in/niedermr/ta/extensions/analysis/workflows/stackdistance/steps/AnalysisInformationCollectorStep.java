package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisInformationCollector;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class AnalysisInformationCollectorStep extends AbstractExecutionStep {

	private boolean m_useMultiFileOutput;

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "ANACOL";
	}

	/** {@link #m_useMultiFileOutput} */
	public void setUseMultiFileOutput(boolean useMultiFileOutput) {
		m_useMultiFileOutput = useMultiFileOutput;
	}

	/** {@inheritDoc} */
	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws ExecutionException {
		final String classPath = configuration.getTestAnalyzerClasspath().getValue() + CP_SEP
				+ getSourceInstrumentedJarFilesClasspath(configuration) + CP_SEP
				+ configuration.getCodePathToTest().getValue() + CP_SEP + configuration.getClasspath().getValue();

		IFullExecutionId fullExecutionId = createFullExecutionId();

		ProgramArgsWriter argsWriter = AnalysisInformationCollector.createProgramArgsWriter();
		argsWriter.setValue(AnalysisInformationCollector.ARGS_EXECUTION_ID, fullExecutionId.getFullId());
		argsWriter.setValue(AnalysisInformationCollector.ARGS_FILE_WITH_TESTS_TO_RUN,
				configuration.getCodePathToTest().getWithAlternativeSeparator(CommonConstants.SEPARATOR_DEFAULT));
		argsWriter.setValue(AnalysisInformationCollector.ARGS_RESULT_FILE,
				getFileInWorkingArea(AnalysisConstants.FILE_OUTPUT_ANALYSIS_INFORMATION));
		argsWriter.setValue(AnalysisInformationCollector.ARGS_TEST_RUNNER_CLASS,
				configuration.getTestRunner().getValue());
		argsWriter.setValue(AnalysisInformationCollector.ARGS_OPERATE_FAULT_TOLERANT,
				configuration.getOperateFaultTolerant().getValueAsString());
		argsWriter.setValue(AnalysisInformationCollector.ARGS_TEST_CLASS_INCLUDES,
				ProcessExecution.wrapPattern(configuration.getTestClassIncludes().getValue()));
		argsWriter.setValue(AnalysisInformationCollector.ARGS_TEST_CLASS_EXCLUDES,
				ProcessExecution.wrapPattern(configuration.getTestClassExcludes().getValue()));
		argsWriter.setValue(AnalysisInformationCollector.ARGS_RESULT_PRESENTATION,
				configuration.getResultPresentation().getValue());
		argsWriter.setValue(AnalysisInformationCollector.ARGS_USE_MULTI_FILE_OUTPUT,
				Boolean.valueOf(m_useMultiFileOutput).toString());

		processExecution.execute(fullExecutionId, ProcessExecution.NO_TIMEOUT, AnalysisInformationCollector.class,
				classPath, argsWriter);
	}

	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(
				getFileInWorkingArea(AnalysisConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), 0,
				configuration.getCodePathToMutate().countElements());
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Analyzing stack distance";
	}
}
