package de.tum.in.niedermr.ta.runner.analysis.workflow.common;

import java.io.IOException;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.infocollection.InformationCollectorParameters;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Base class for a step to start an InformationCollector. */
public abstract class AbstractInformationCollectorStep extends AbstractExecutionStep {

	private boolean m_useMultiFileOutput;

	/** {@link #m_useMultiFileOutput} */
	public void setUseMultiFileOutput(boolean useMultiFileOutput) {
		m_useMultiFileOutput = useMultiFileOutput;
	}

	/** {@link #m_useMultiFileOutput} */
	public boolean isUseMultiFileOutput() {
		return m_useMultiFileOutput;
	}

	protected abstract Class<?> getInformationCollectorClass();

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

		processExecution.execute(executionId, ProcessExecution.NO_TIMEOUT, getInformationCollectorClass(), classPath,
				argsWriter);
	}

	/** Create the program args for the execution. */
	protected ProgramArgsWriter createProgramArgs(Configuration configuration, IFullExecutionId executionId) {
		ProgramArgsWriter argsWriter = InformationCollectorParameters.createProgramArgsWriter();
		argsWriter.setValue(InformationCollectorParameters.ARGS_EXECUTION_ID, executionId.getFullId());
		argsWriter.setValue(InformationCollectorParameters.ARGS_FILE_WITH_TESTS_TO_RUN,
				configuration.getCodePathToTest().getWithAlternativeSeparator(CommonConstants.SEPARATOR_DEFAULT));
		argsWriter.setValue(InformationCollectorParameters.ARGS_FILE_WITH_RESULTS, getFileWithResultsParameterValue());
		argsWriter.setValue(InformationCollectorParameters.ARGS_TEST_RUNNER_CLASS,
				configuration.getTestRunner().getValue());
		argsWriter.setValue(InformationCollectorParameters.ARGS_OPERATE_FAULT_TOLERANT,
				configuration.getOperateFaultTolerant().getValueAsString());
		argsWriter.setValue(InformationCollectorParameters.ARGS_TEST_CLASS_INCLUDES,
				ProcessExecution.wrapPattern(configuration.getTestClassIncludes().getValue()));
		argsWriter.setValue(InformationCollectorParameters.ARGS_TEST_CLASS_EXCLUDES,
				ProcessExecution.wrapPattern(configuration.getTestClassExcludes().getValue()));
		argsWriter.setValue(InformationCollectorParameters.ARGS_RESULT_PRESENTATION,
				configuration.getResultPresentation().getValue());
		argsWriter.setValue(InformationCollectorParameters.ARGS_USE_MULTI_FILE_OUTPUT,
				Boolean.valueOf(m_useMultiFileOutput).toString());
		return argsWriter;
	}

	protected abstract String getFileWithResultsParameterValue();

	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return configuration.getCodePathToMutate().getValue();
	}

	protected String getTestInstrumentedJarFilesClasspath(Configuration configuration) {
		return configuration.getCodePathToTest().getValue();
	}
}
