package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s2;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisInformationCollector;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;

public class AnalysisInformationCollectorStep extends AbstractExecutionStep {

	@Override
	protected String getSuffixForFullExecutionId() {
		return "ANACOL";
	}

	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws Exception {
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

		processExecution.execute(fullExecutionId, ProcessExecution.NO_TIMEOUT,
				AnalysisInformationCollector.class.getName(), classPath, argsWriter);
	}

	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(
				getFileInWorkingArea(AnalysisConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), 0,
				configuration.getCodePathToMutate().countElements());
	}

	@Override
	protected String getDescription() {
		return "Analyzing stack distance";
	}
}
