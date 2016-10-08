package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import java.io.IOException;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.InformationCollector;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class InformationCollectorStep extends AbstractExecutionStep {

	private boolean m_useMultiFileOutput;

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "INFCOL";
	}

	/** {@link #m_useMultiFileOutput} */
	public void setUseMultiFileOutput(boolean useMultiFileOutput) {
		m_useMultiFileOutput = useMultiFileOutput;
	}

	/** {@link #m_useMultiFileOutput} */
	public boolean isUseMultiFileOutput() {
		return m_useMultiFileOutput;
	}

	/** {@inheritDoc} */
	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, IOException {
		final String classPath = configuration.getTestAnalyzerClasspath().getValue() + CP_SEP
				+ getSourceInstrumentedJarFilesClasspath(configuration) + CP_SEP
				+ getTestInstrumentedJarFilesClasspath(configuration) + CP_SEP
				+ configuration.getClasspath().getValue();

		IFullExecutionId executionId = createFullExecutionId();

		ProgramArgsWriter argsWriter = createProgramArgs(configuration, executionId);

		processExecution.execute(executionId, ProcessExecution.NO_TIMEOUT, InformationCollector.class, classPath,
				argsWriter);
	}

	/** Create the program args for the execution. */
	protected ProgramArgsWriter createProgramArgs(Configuration configuration, IFullExecutionId executionId) {
		ProgramArgsWriter argsWriter = InformationCollector.createProgramArgsWriter();
		argsWriter.setValue(InformationCollector.ARGS_EXECUTION_ID, executionId.getFullId());
		argsWriter.setValue(InformationCollector.ARGS_FILE_WITH_TESTS_TO_RUN,
				configuration.getCodePathToTest().getWithAlternativeSeparator(CommonConstants.SEPARATOR_DEFAULT));
		argsWriter.setValue(InformationCollector.ARGS_FILE_WITH_RESULTS,
				getFileInWorkingArea(FILE_OUTPUT_COLLECTED_INFORMATION));
		argsWriter.setValue(InformationCollector.ARGS_TEST_RUNNER_CLASS, configuration.getTestRunner().getValue());
		argsWriter.setValue(InformationCollector.ARGS_OPERATE_FAULT_TOLERANT,
				configuration.getOperateFaultTolerant().getValueAsString());
		argsWriter.setValue(InformationCollector.ARGS_TEST_CLASS_INCLUDES,
				ProcessExecution.wrapPattern(configuration.getTestClassIncludes().getValue()));
		argsWriter.setValue(InformationCollector.ARGS_TEST_CLASS_EXCLUDES,
				ProcessExecution.wrapPattern(configuration.getTestClassExcludes().getValue()));
		argsWriter.setValue(InformationCollector.ARGS_RESULT_PRESENTATION,
				configuration.getResultPresentation().getValue());
		argsWriter.setValue(InformationCollector.ARGS_USE_MULTI_FILE_OUTPUT,
				Boolean.valueOf(m_useMultiFileOutput).toString());
		return argsWriter;
	}

	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(getFileInWorkingArea(FILE_TEMP_JAR_INSTRUMENTED_SOURCE_X), 0,
				configuration.getCodePathToMutate().countElements());
	}

	protected String getTestInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(getFileInWorkingArea(FILE_TEMP_JAR_INSTRUMENTED_TEST_X), 0,
				configuration.getCodePathToTest().countElements());
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Loading information about the testcases and the methods they are invoking";
	}
}
