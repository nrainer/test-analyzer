package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s4;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.conqat.lib.commons.io.ProcessUtils.ExecutionResult;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.TestRun;
import de.tum.in.niedermr.ta.runner.analysis.mutation.MethodMutation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ProcessExecutionFailedException;
import de.tum.in.niedermr.ta.runner.execution.exceptions.TimeoutException;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;

public class MutateAndTestStep extends AbstractExecutionStep {
	static final Logger LOG = LogManager.getLogger(MutateAndTestStep.class);
	private static final Logger LOG_TEST_SYS_ERR = LogManager.getLogger("TestSysErr");

	protected static final String EXEC_ID_TEST_RUN = "TSTRUN";
	protected static final int TIME_INTERVAL_ABORT_CHECK = 30;

	protected ConcurrentLinkedQueue<TestInformation> m_methodsToMutateAndTestsToRun;
	protected boolean m_aborted;
	protected IReturnValueGenerator[] m_returnValueGenerators;

	public void setInputData(ConcurrentLinkedQueue<TestInformation> methodsToMutateAndTestsToRun) {
		this.m_methodsToMutateAndTestsToRun = methodsToMutateAndTestsToRun;
	}

	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws Exception {
		LOG.info("Using " + LoggingUtil.appendPluralS(configuration.getNumberOfThreads().getValue(), "thread", true)
				+ " for mutating and testing.");
		LOG.info(LoggingUtil.appendPluralS(m_methodsToMutateAndTestsToRun, "method", true)
				+ " are candidates to be mutated (Filters have not been applied yet).");

		loadReturnValueGenerators(configuration);

		startAbortChecker();

		List<MutateAndTestThread> threadList = createAndStartThreads(configuration, processExecution);

		int countSuccessful = 0;
		int countSkipped = 0;
		int countTimeout = 0;
		int countError = 0;

		for (MutateAndTestThread workerThread : threadList) {
			workerThread.join();

			countSuccessful += workerThread.m_countSuccessful;
			countSkipped += workerThread.m_countSkipped;
			countTimeout += workerThread.m_countTimeout;
			countError += workerThread.m_countError;
		}

		if (m_aborted) {
			LOG.warn("MANUALLY ABORTED.");
			throw new ExecutionException(getFullExecId(EXEC_ID_TEST_RUN), "Aborted");
		} else {
			String summary = getSummary(countSuccessful, countSkipped, countTimeout, countError);
			LOG.info("ALL THREADS FINISHED. " + summary);
			writeSummaryToFile(configuration, summary);
		}
	}

	/**
	 * Write the summary of the test execution to the file
	 * {@link EnvironmentConstants#FILE_OUTPUT_EXECUTION_INFORMATION}.
	 */
	private void writeSummaryToFile(Configuration configuration, String summary) {
		try {
			IResultPresentation resultPresentation = configuration.getResultPresentation()
					.createInstance(getExecutionId());
			String sqlStatement = resultPresentation.formatExecutionSummary(summary);
			TextFileData.appendToFile(getFileInWorkingArea(FILE_OUTPUT_EXECUTION_INFORMATION),
					Arrays.asList(sqlStatement));
		} catch (ReflectiveOperationException | IOException e) {
			LOG.error("When writing the summary to the file", e);
		}
	}

	private void startAbortChecker() {
		this.m_aborted = false;

		String fileName = getFileInWorkingArea(EnvironmentConstants.FILE_TEMP_IS_RUNNING_TESTS);

		AbortCheckerThread abortCheckerThread = new AbortCheckerThread(fileName, TIME_INTERVAL_ABORT_CHECK) {
			@Override
			protected void execAbort() {
				m_methodsToMutateAndTestsToRun.clear();
				m_aborted = true;
			}
		};

		abortCheckerThread.start();
	}

	private void loadReturnValueGenerators(Configuration configuration) throws ExecutionException {
		try {
			this.m_returnValueGenerators = configuration.getReturnValueGenerators().createInstances();
		} catch (ReflectiveOperationException ex) {
			throw new ExecutionException(getFullExecId(EXEC_ID_TEST_RUN),
					"Return value generator is not on the classpath (" + ex.getMessage() + ")");
		}
	}

	private List<MutateAndTestThread> createAndStartThreads(Configuration configuration,
			ProcessExecution processExecution) {
		int numberOfThreads = configuration.getNumberOfThreads().getValue();
		List<MutateAndTestThread> threadList = new LinkedList<>();

		for (int index = 0; index < numberOfThreads; index++) {
			MutateAndTestThread workerThread = new MutateAndTestThread(index, configuration, processExecution);

			threadList.add(workerThread);
			workerThread.start();
		}

		return threadList;
	}

	@Override
	protected String getDescription() {
		return "Mutating methods and running testcases";
	}

	private static String getSummary(int countSuccessful, int countSkipped, int countTimeout, int countError) {
		return "(" + (countSuccessful + countSkipped + countTimeout + countError) + " methods. " + countSuccessful
				+ " processed successfully. " + countSkipped + " skipped. " + countTimeout + " with timeout. "
				+ countError + " failed.)";
	}

	/** Worker thread that polls methods to mutate and triggers the test executions. */
	protected class MutateAndTestThread extends Thread {
		private final int m_threadIndex;
		private final Configuration m_configuration;
		private final ProcessExecution m_processExecution;
		private int m_countMethods;
		private int m_countSuccessful;
		private int m_countSkipped;
		private int m_countTimeout;
		private int m_countError;

		private MethodIdentifier m_currentMethodUnderTest;
		private Set<TestcaseIdentifier> m_currentTestcases;

		public MutateAndTestThread(int index, Configuration configuration, ProcessExecution processExecution) {
			m_threadIndex = index;
			m_configuration = configuration;
			m_processExecution = processExecution;

			this.m_countMethods = 0;
			this.m_countSuccessful = 0;
			this.m_countSkipped = 0;
			this.m_countTimeout = 0;
			this.m_countError = 0;
		}

		@Override
		public void run() {
			while (true) {
				TestInformation tInformation = m_methodsToMutateAndTestsToRun.poll();

				if (tInformation == null) {
					break;
				}

				this.m_currentMethodUnderTest = tInformation.getMethodUnderTest();
				this.m_currentTestcases = tInformation.getTestcases();

				mutateAndTest();

				m_countMethods++;
			}

			LOG.info("THREAD FINISHED: T_" + m_threadIndex + " "
					+ getSummary(m_countSuccessful, m_countSkipped, m_countTimeout, m_countError));
		}

		protected void mutateAndTest() {
			int retValGenIndex = 0;

			for (IReturnValueGenerator returnValueGen : m_returnValueGenerators) {
				final String fullExecutionId = getFullExecId(
						EXEC_ID_TEST_RUN + "_T" + m_threadIndex + "_C" + m_countMethods + "_R" + retValGenIndex);

				mutateAndTest(fullExecutionId, returnValueGen);

				retValGenIndex++;
			}
		}

		protected void mutateAndTest(String fullExecutionId, IReturnValueGenerator returnValueGenerator) {
			try {
				mutateAndTestInternal(fullExecutionId, returnValueGenerator);
			} catch (TimeoutException ex) {
				LOG.error("Mutate and test failed due to timeout (" + ex.getMessage() + "): "
						+ m_currentMethodUnderTest.get());
				handleAbortedTestExecution(returnValueGenerator, TestAbortReason.TEST_TIMEOUT);
				m_countTimeout++;
			} catch (ProcessExecutionFailedException ex) {
				LOG.error("Test execution did not complete: " + m_currentMethodUnderTest.get(), ex);
				handleAbortedTestExecution(returnValueGenerator, TestAbortReason.TEST_DIED);
				m_countError++;
			} catch (Exception ex) {
				LOG.error("Mutate and test failed: " + m_currentMethodUnderTest.get(), ex);
				m_countError++;
			}
		}

		private void mutateAndTestInternal(String fullExecutionId, IReturnValueGenerator returnValueGenerator)
				throws Exception {
			LOG.info("Trying to mutate " + m_currentMethodUnderTest.get() + " with return type generator "
					+ returnValueGenerator.getClass().getName());

			boolean wasSuccessfullyMutated = MethodMutation.createJarWithMutatedMethod(m_currentMethodUnderTest,
					getFileInWorkingArea(getWithIndex(FILE_TEMP_JAR_X, m_threadIndex)), returnValueGenerator,
					m_configuration.getMethodFilters().createInstances());

			if (wasSuccessfullyMutated) {
				handleSuccessfullyMutatedMethod(fullExecutionId, returnValueGenerator);
			} else {
				LOG.info("Skipped: " + m_currentMethodUnderTest.get());
				m_countSkipped++;
			}
		}

		protected void handleSuccessfullyMutatedMethod(String fullExecutionId,
				IReturnValueGenerator returnValueGenerator) throws IOException {
			LOG.info("Mutated: " + m_currentMethodUnderTest.get());

			String fileWithTestsToRun = getWithIndex(EnvironmentConstants.FILE_TEMP_TESTS_TO_RUN_X, m_threadIndex);
			TextFileData.writeToFile(getFileInWorkingArea(fileWithTestsToRun), testcasesToStringList());

			LOG.info("Testing: " + m_currentMethodUnderTest.get() + " with "
					+ LoggingUtil.appendPluralS(m_currentTestcases, "testcase", true) + ".");

			runTestsAndRecordResult(fullExecutionId, fileWithTestsToRun,
					getWithIndex(EnvironmentConstants.FILE_TEMP_RESULT_X, m_threadIndex), returnValueGenerator);
			m_countSuccessful++;
		}

		protected void handleAbortedTestExecution(IReturnValueGenerator returnValueGenerator,
				TestAbortReason abortType) {
			try {
				IResultPresentation resultPresentation = m_configuration.getResultPresentation()
						.createInstance(getExecutionId());

				String testAbortInformation = resultPresentation.formatTestAbortEntry(m_currentMethodUnderTest,
						returnValueGenerator.getClass().getName(), abortType);

				String fileWithResults = getFileInWorkingArea(
						getWithIndex(EnvironmentConstants.FILE_TEMP_RESULT_X, m_threadIndex));
				TextFileData.appendToFile(fileWithResults, Arrays.asList(testAbortInformation));

			} catch (ReflectiveOperationException | IOException e) {
				LOG.error("handleAbortedTestExecution", e);
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
		 * Note that the full original classpath is used. However, the mutated jar is inserted at the beginning of the
		 * classpath, thus the mutated class is considered first in that jar file.
		 */
		protected void runTestsAndRecordResult(String fullExecutionId, String fileWithTestsToRun,
				String fileWithResults, IReturnValueGenerator retValGen) throws IOException {
			final String usedReturnValueGenerator = retValGen.getClass().getName();
			final String classPath = m_configuration.getTestAnalyzerClasspath().getValue() + CP_SEP
					+ getFileInWorkingArea(getWithIndex(FILE_TEMP_JAR_X, m_threadIndex)) + CP_SEP
					+ m_configuration.getFullClasspath();
			final int timeout = m_configuration.computeTestingTimeout(m_currentTestcases.size());

			ProgramArgsWriter argsWriter = TestRun.createProgramArgsWriter();
			argsWriter.setValue(TestRun.ARGS_EXECUTION_ID, fullExecutionId);
			argsWriter.setValue(TestRun.ARGS_FILE_WITH_TESTS_TO_RUN, getFileInWorkingArea(fileWithTestsToRun));
			argsWriter.setValue(TestRun.ARGS_FILE_WITH_RESULTS, getFileInWorkingArea(fileWithResults));
			argsWriter.setValue(TestRun.ARGS_MUTATED_METHOD_IDENTIFIER, m_currentMethodUnderTest.get());
			argsWriter.setValue(TestRun.ARGS_TEST_RUNNER_CLASS, m_configuration.getTestRunner().getValue());
			argsWriter.setValue(TestRun.ARGS_RETURN_VALUE_GENERATOR_CLASS, usedReturnValueGenerator);
			argsWriter.setValue(TestRun.ARGS_RESULT_PRESENTATION, m_configuration.getResultPresentation().getValue());

			ExecutionResult executionResult = m_processExecution.execute(fullExecutionId, timeout,
					TestRun.class.getName(), classPath, argsWriter);

			if (!executionResult.getStderr().isEmpty()) {
				LOG_TEST_SYS_ERR.debug("SYSERR when running test on mutated method " + m_currentMethodUnderTest.get()
						+ " with " + usedReturnValueGenerator + ": "
						+ LoggingUtil.shorten(300, executionResult.getStderr()));
			}
		}

		@Override
		public String toString() {
			return MutateAndTestThread.class.getSimpleName() + " [index = " + m_threadIndex + "]";
		}
	}
}
