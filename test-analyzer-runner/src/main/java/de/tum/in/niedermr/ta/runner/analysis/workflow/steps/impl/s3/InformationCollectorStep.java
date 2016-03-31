package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s3;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.InformationCollector;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.infocollection.CollectedInformation;

public class InformationCollectorStep extends AbstractExecutionStep {
	private static final String EXEC_ID = "INFCOL";

	private final ConcurrentLinkedQueue<TestInformation> m_methodsToMutateAndTestsToRun;

	public InformationCollectorStep() {
		this.m_methodsToMutateAndTestsToRun = new ConcurrentLinkedQueue<>();
	}

	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws Exception {
		final String classPath = configuration.getTestAnalyzerClasspath().getValue() + CP_SEP
				+ getSourceInstrumentedJarFilesClasspath(configuration) + CP_SEP
				+ getTestInstrumentedJarFilesClasspath(configuration) + CP_SEP
				+ configuration.getClasspath().getValue();

		String executionId = getFullExecId(EXEC_ID);

		ProgramArgsWriter argsWriter = InformationCollector.createProgramArgsWriter();
		argsWriter.setValue(InformationCollector.ARGS_EXECUTION_ID, executionId);
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

		processExecution.execute(executionId, ProcessExecution.NO_TIMEOUT, InformationCollector.class.getName(),
				classPath, argsWriter);

		loadCollectedData();
	}

	protected void loadCollectedData() throws IOException {
		List<String> data = TextFileData.readFromFile(getFileInWorkingArea(FILE_OUTPUT_COLLECTED_INFORMATION));

		m_methodsToMutateAndTestsToRun.addAll(CollectedInformation.parseInformationCollectorData(data));
	}

	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(getFileInWorkingArea(FILE_TEMP_JAR_INSTRUMENTED_SOURCE_X), 0,
				configuration.getCodePathToMutate().countElements());
	}

	protected String getTestInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(getFileInWorkingArea(FILE_TEMP_JAR_INSTRUMENTED_TEST_X), 0,
				configuration.getCodePathToTest().countElements());
	}

	public ConcurrentLinkedQueue<TestInformation> getMethodsToMutateAndTestsToRun() {
		return m_methodsToMutateAndTestsToRun;
	}

	@Override
	protected String getDescription() {
		return "Loading information about the testcases and the methods they are invoking";
	}
}
