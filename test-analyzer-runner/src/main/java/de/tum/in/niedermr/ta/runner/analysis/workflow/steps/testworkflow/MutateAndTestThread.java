package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.conqat.lib.commons.io.ProcessUtils.ExecutionResult;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.TestRun;
import de.tum.in.niedermr.ta.runner.analysis.mutation.MethodMutation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ProcessExecutionFailedException;
import de.tum.in.niedermr.ta.runner.execution.exceptions.TimeoutException;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;

/**
 * Worker thread that polls methods to mutate and triggers the test executions.
 */
class MutateAndTestThread extends Thread {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(MutateAndTestThread.class);

	/** Logger for test errors. */
	private static final Logger LOG_TEST_SYS_ERR = LogManager.getLogger("TestSysErr");

	private final ExecutionContext m_context;
	private final int m_threadIndex;
	private final Configuration m_configuration;
	private final ProcessExecution m_processExecution;
	private final MethodProcessingStatistics m_statistics;
	private final Queue<TestInformation> m_methodsToMutateAndTestsToRun;
	private final IReturnValueGenerator[] m_returnValueGenerators;
	private final String m_suffixForFullExecutionId;

	private MethodIdentifier m_currentMethodUnderTest;
	private Set<TestcaseIdentifier> m_currentTestcases;

	/** Constructor. */
	public MutateAndTestThread(int threadIndex, ExecutionContext context, ProcessExecution processExecution,
			Queue<TestInformation> methodsToMutateAndTestsToRun, String suffixForFullExecutionId) {
		m_threadIndex = threadIndex;
		m_context = context;
		m_configuration = context.getConfiguration();
		m_processExecution = processExecution;
		m_methodsToMutateAndTestsToRun = methodsToMutateAndTestsToRun;
		m_suffixForFullExecutionId = suffixForFullExecutionId;

		// requires the configuration to be set
		m_returnValueGenerators = createReturnValueGenerators();

		m_statistics = new MethodProcessingStatistics();
	}

	/** {@link #m_statistics} */
	public MethodProcessingStatistics getStatistics() {
		return m_statistics;
	}

	/** {@inheritDoc} */
	@Override
	public void run() {
		while (true) {
			TestInformation tInformation = m_methodsToMutateAndTestsToRun.poll();

			if (tInformation == null) {
				break;
			}

			m_currentMethodUnderTest = tInformation.getMethodUnderTest();
			m_currentTestcases = tInformation.getTestcases();

			mutateAndTest();

			m_statistics.incrementMethodCount();
		}

		LOGGER.info("THREAD FINISHED: T_" + m_threadIndex + " (" + m_statistics.toSummary() + ")");
	}

	protected void mutateAndTest() {
		int retValGenIndex = 0;

		for (IReturnValueGenerator returnValueGen : m_returnValueGenerators) {
			IFullExecutionId fullExecutionId = m_context.getExecutionId()
					.createFullExecutionId(m_suffixForFullExecutionId + "_T" + m_threadIndex + "_C"
							+ m_statistics.getMethodCount() + "_R" + retValGenIndex);

			mutateAndTest(fullExecutionId, returnValueGen);

			retValGenIndex++;
		}
	}

	protected void mutateAndTest(IFullExecutionId fullExecutionId, IReturnValueGenerator returnValueGenerator) {
		try {
			mutateAndTestInternal(fullExecutionId, returnValueGenerator);
		} catch (TimeoutException ex) {
			LOGGER.error("Mutate and test failed due to timeout (" + ex.getMessage() + "): "
					+ m_currentMethodUnderTest.get());
			handleAbortedTestExecution(returnValueGenerator, TestAbortReason.TEST_TIMEOUT);
			m_statistics.incrementTimeoutCount();
		} catch (ProcessExecutionFailedException ex) {
			LOGGER.error("Test execution did not complete: " + m_currentMethodUnderTest.get(), ex);
			handleAbortedTestExecution(returnValueGenerator, TestAbortReason.TEST_DIED);
			m_statistics.incrementErrorCount();
		} catch (Exception ex) {
			LOGGER.error("Mutate and test failed: " + m_currentMethodUnderTest.get(), ex);
			m_statistics.incrementErrorCount();
		}
	}

	private void mutateAndTestInternal(IFullExecutionId fullExecutionId, IReturnValueGenerator returnValueGenerator)
			throws Exception {
		LOGGER.info("Trying to mutate " + m_currentMethodUnderTest.get() + " with return type generator "
				+ returnValueGenerator.getClass().getName());

		boolean wasSuccessfullyMutated = MethodMutation.createJarWithMutatedMethod(m_currentMethodUnderTest,
				getFileInWorkingArea(Environment.getWithIndex(EnvironmentConstants.FILE_TEMP_JAR_X, m_threadIndex)),
				returnValueGenerator, m_configuration.getMethodFilters().createInstances());

		if (wasSuccessfullyMutated) {
			handleSuccessfullyMutatedMethod(fullExecutionId, returnValueGenerator);
		} else {
			LOGGER.info("Skipped: " + m_currentMethodUnderTest.get());
			m_statistics.incrementSkippedCount();
		}
	}

	protected void handleSuccessfullyMutatedMethod(IFullExecutionId fullExecutionId,
			IReturnValueGenerator returnValueGenerator) throws IOException {
		LOGGER.info("Mutated: " + m_currentMethodUnderTest.get());

		String fileWithTestsToRun = Environment.getWithIndex(EnvironmentConstants.FILE_TEMP_TESTS_TO_RUN_X,
				m_threadIndex);
		TextFileData.writeToFile(getFileInWorkingArea(fileWithTestsToRun), testcasesToStringList());

		LOGGER.info("Testing " + m_currentMethodUnderTest.get() + " with " + m_currentTestcases.size() + " testcases.");

		runTestsAndRecordResult(fullExecutionId, fileWithTestsToRun,
				Environment.getWithIndex(EnvironmentConstants.FILE_TEMP_RESULT_X, m_threadIndex), returnValueGenerator);
		m_statistics.incrementSuccessfulCount();
	}

	protected void handleAbortedTestExecution(IReturnValueGenerator returnValueGenerator, TestAbortReason abortType) {
		try {
			IResultPresentation resultPresentation = m_configuration.getResultPresentation()
					.createInstance(m_context.getExecutionId());

			String testAbortInformation = resultPresentation.formatTestAbortEntry(m_currentMethodUnderTest,
					returnValueGenerator.getClass().getName(), abortType);

			String fileWithResults = getFileInWorkingArea(
					Environment.getWithIndex(EnvironmentConstants.FILE_TEMP_RESULT_X, m_threadIndex));
			TextFileData.appendToFile(fileWithResults, Arrays.asList(testAbortInformation));

		} catch (ReflectiveOperationException | IOException e) {
			LOGGER.error("handleAbortedTestExecution", e);
		}
	}

	protected final List<String> testcasesToStringList() {
		List<String> result = new LinkedList<>();

		for (TestcaseIdentifier testcase : m_currentTestcases) {
			result.add(testcase.get());
		}

		return result;
	}

	/**
	 * Run the test cases and record the result.
	 * 
	 * Note that the full original classpath is used. However, the mutated jar is inserted at the beginning of the
	 * classpath, thus the mutated class is considered first in that jar file.
	 */
	protected void runTestsAndRecordResult(IFullExecutionId fullExecutionId, String fileWithTestsToRun,
			String fileWithResults, IReturnValueGenerator retValGen) throws IOException {
		String usedReturnValueGenerator = retValGen.getClass().getName();
		String classPath = m_configuration.getTestAnalyzerClasspath().getValue() + FileSystemConstants.CP_SEP
				+ getFileInWorkingArea(Environment.getWithIndex(EnvironmentConstants.FILE_TEMP_JAR_X, m_threadIndex))
				+ FileSystemConstants.CP_SEP + m_configuration.getFullClasspath();
		int timeout = m_configuration.computeTestingTimeout(m_currentTestcases.size());

		ProgramArgsWriter argsWriter = TestRun.createProgramArgsWriter();
		argsWriter.setValue(TestRun.ARGS_EXECUTION_ID, fullExecutionId.getFullId());
		argsWriter.setValue(TestRun.ARGS_FILE_WITH_TESTS_TO_RUN, getFileInWorkingArea(fileWithTestsToRun));
		argsWriter.setValue(TestRun.ARGS_FILE_WITH_RESULTS, getFileInWorkingArea(fileWithResults));
		argsWriter.setValue(TestRun.ARGS_MUTATED_METHOD_IDENTIFIER, m_currentMethodUnderTest.get());
		argsWriter.setValue(TestRun.ARGS_TEST_RUNNER_CLASS, m_configuration.getTestRunner().getValue());
		argsWriter.setValue(TestRun.ARGS_RETURN_VALUE_GENERATOR_CLASS, usedReturnValueGenerator);
		argsWriter.setValue(TestRun.ARGS_RESULT_PRESENTATION, m_configuration.getResultPresentation().getValue());

		ExecutionResult executionResult = runTestsInNewProcess(fullExecutionId, classPath, timeout, argsWriter);

		if (!executionResult.getStderr().isEmpty()) {
			LOG_TEST_SYS_ERR
					.debug("SYSERR when running test on mutated method " + m_currentMethodUnderTest.get() + " with "
							+ usedReturnValueGenerator + ": " + LoggingUtil.shorten(300, executionResult.getStderr()));
		}
	}

	/** Run the tests in a new process. */
	protected ExecutionResult runTestsInNewProcess(IFullExecutionId fullExecutionId, final String classPath,
			final int timeout, ProgramArgsWriter argsWriter) {
		return m_processExecution.execute(fullExecutionId, timeout, TestRun.class, classPath, argsWriter);
	}

	protected IReturnValueGenerator[] createReturnValueGenerators() throws ExecutionException {
		try {
			return m_configuration.getReturnValueGenerators().createInstances();
		} catch (ReflectiveOperationException ex) {
			throw new ExecutionException(m_context.getExecutionId().createFullExecutionId(m_suffixForFullExecutionId),
					"Return value generator is not on the classpath (" + ex.getMessage() + ")");
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MutateAndTestThread.class.getSimpleName() + " [index = " + m_threadIndex + "]";
	}

	private String getFileInWorkingArea(String fileName) {
		return AbstractWorkflow.getFileInWorkingArea(m_context, fileName);
	}
}