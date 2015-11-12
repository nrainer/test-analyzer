package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s4;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.runner.analysis.TestRun;
import de.tum.in.niedermr.ta.runner.analysis.mutation.MethodMutation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.TimeoutException;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;

public class MutateAndTestStep extends AbstractExecutionStep {
	protected static final Logger LOG_TEST_SYSERR = LogManager.getLogger("LoggerTestingSyserr");

	protected static final String EXEC_ID_TEST_RUN = "TSTRUN";
	protected static final int TIME_INTERVAL_ABORT_CHECK = 30;

	protected ConcurrentLinkedQueue<TestInformation> m_methodsToMutateAndTestsToRun;
	protected boolean m_aborted;
	protected IReturnValueGenerator[] m_returnValueGenerators;

	public MutateAndTestStep(ExecutionInformation information) {
		super(information);
	}

	public void setInputData(ConcurrentLinkedQueue<TestInformation> methodsToMutateAndTestsToRun) {
		this.m_methodsToMutateAndTestsToRun = methodsToMutateAndTestsToRun;
	}

	@Override
	public void runInternal() throws Exception {
		LOG.info("Using " + LoggingUtil.appendPluralS(m_configuration.getNumberOfThreads().getValue(), "thread", true)
				+ " for mutating and testing.");
		LOG.info(LoggingUtil.appendPluralS(m_methodsToMutateAndTestsToRun, "method", true)
				+ " are candidates to be mutated (Filters have not been applied yet).");

		loadReturnValueGenerators();

		startAbortChecker();

		List<MutateAndTestThread> threadList = createAndStartThreads(m_configuration.getNumberOfThreads().getValue());

		int countSuccessful = 0;
		int countSkipped = 0;
		int countTimeout = 0;
		int countError = 0;

		for (MutateAndTestThread t : threadList) {
			t.join();

			countSuccessful += t.m_countSuccessful;
			countSkipped += t.m_countSkipped;
			countTimeout += t.m_countTimeout;
			countError += t.m_countError;
		}

		if (m_aborted) {
			LOG.warn("MANUALLY ABORTED.");
			throw new FailedExecution(getFullExecId(EXEC_ID_TEST_RUN), "Aborted");
		} else {
			LOG.info("ALL THREADS FINISHED. " + getSummary(countSuccessful, countSkipped, countTimeout, countError));
		}
	}

	private void startAbortChecker() {
		this.m_aborted = false;

		new AbortCheckerThread().start();
	}

	private void loadReturnValueGenerators() throws FailedExecution {
		try {
			this.m_returnValueGenerators = m_configuration.getReturnValueGenerators().createInstances();
		} catch (ReflectiveOperationException ex) {
			throw new FailedExecution(getFullExecId(EXEC_ID_TEST_RUN),
					"Return value generator is not on the classpath (" + ex.getMessage() + ")");
		}
	}

	private List<MutateAndTestThread> createAndStartThreads(int numberOfThreads) {
		List<MutateAndTestThread> threadList = new LinkedList<>();

		for (int i = 0; i < numberOfThreads; i++) {
			MutateAndTestThread t = new MutateAndTestThread(i);

			threadList.add(t);
			t.start();
		}

		return threadList;
	}

	protected MutateAndTestThread createMutateAndTestThread(int index) {
		return new MutateAndTestThread(index);
	}

	@Override
	protected String getDescription() {
		return "Mutating methods and running testcases";
	}

	private String getSummary(int countSuccessful, int countSkipped, int countTimeout, int countError) {
		return "(" + (countSuccessful + countSkipped + countTimeout + countError) + " methods. " + countSuccessful
				+ " processed successfully. " + countSkipped + " skipped. " + countTimeout + " with timeout. "
				+ countError + " failed.)";
	}

	protected class MutateAndTestThread extends Thread {
		private final int m_threadIndex;
		private int m_countMethods;
		private int m_countSuccessful;
		private int m_countSkipped;
		private int m_countTimeout;
		private int m_countError;

		private MethodIdentifier m_currentMethodUnderTest;
		private Set<TestcaseIdentifier> m_currentTestcases;

		public MutateAndTestThread(int index) {
			this.m_threadIndex = index;

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
				final String testingId = getFullExecId(
						EXEC_ID_TEST_RUN + "_T" + m_threadIndex + "_C" + m_countMethods + "_R" + retValGenIndex);

				mutateAndTestInternal(testingId, returnValueGen);

				retValGenIndex++;
			}
		}

		protected void mutateAndTestInternal(String testingId, IReturnValueGenerator returnValueGenerator) {
			LOG.info("Trying to mutate " + m_currentMethodUnderTest.get() + " with return type generator "
					+ returnValueGenerator.getClass().getName());

			try {
				boolean wasSuccessfullyMutated = MethodMutation.createJarWithMutatedMethod(m_currentMethodUnderTest,
						getFileInWorkingArea(getWithIndex(FILE_TEMP_JAR_X, m_threadIndex)), returnValueGenerator,
						m_configuration.getMethodFilters().createInstances());

				if (wasSuccessfullyMutated) {
					LOG.info("Mutated: " + m_currentMethodUnderTest.get());

					String fileWithTestsToRun = getWithIndex(EnvironmentConstants.FILE_TEMP_TESTS_TO_RUN_X,
							m_threadIndex);
					TextFileData.writeToFile(getFileInWorkingArea(fileWithTestsToRun), testcasesToStringList());

					LOG.info("Testing: " + m_currentMethodUnderTest.get() + " with "
							+ LoggingUtil.appendPluralS(m_currentTestcases, "testcase", true) + ".");

					runTestsAndRecordResult(testingId, fileWithTestsToRun,
							getWithIndex(EnvironmentConstants.FILE_TEMP_RESULT_X, m_threadIndex), returnValueGenerator);
					m_countSuccessful++;
				} else {
					LOG.info("Skipped: " + m_currentMethodUnderTest.get());
					m_countSkipped++;
				}
			} catch (TimeoutException ex) {
				LOG.error("Mutate and test failed due to timeout (" + ex.getMessage() + "): "
						+ m_currentMethodUnderTest.get());
				m_countTimeout++;
			} catch (Exception ex) {
				LOG.error("Mutate and test failed: " + m_currentMethodUnderTest.get(), ex);
				m_countError++;
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
		protected void runTestsAndRecordResult(String execId, String fileWithTestsToRun, String fileWithResults,
				IReturnValueGenerator retValGen) throws IOException {
			final String usedReturnValueGenerator = retValGen.getClass().getName();
			final String classPath = ClasspathUtility.getProgramClasspath()
					+ getFileInWorkingArea(getWithIndex(FILE_TEMP_JAR_X, m_threadIndex)) + CP_SEP
					+ m_configuration.getFullClasspath();
			final int timeout = m_configuration.computeTestingTimeout(m_currentTestcases.size());

			List<String> arguments = new LinkedList<>();
			arguments.add(getFileInWorkingArea(fileWithTestsToRun));
			arguments.add(getFileInWorkingArea(fileWithResults));
			arguments.add(m_currentMethodUnderTest.get());
			arguments.add(m_configuration.getTestRunner().getValue());
			arguments.add(usedReturnValueGenerator);
			arguments.add(m_configuration.getResultPresentation().getValue());

			String sysErr = m_processExecution.executeAndGetSyserr(execId, timeout, getClassNameOfTestRun(), classPath,
					arguments);

			if (!sysErr.isEmpty()) {
				LOG_TEST_SYSERR.debug("SYSERR when running test on mutated method " + m_currentMethodUnderTest.get()
						+ " with " + usedReturnValueGenerator + ": " + LoggingUtil.shorten(300, sysErr));
			}
		}

		protected String getClassNameOfTestRun() {
			return TestRun.class.getName();
		}

		@Override
		public String toString() {
			return MutateAndTestThread.class.getSimpleName() + " [index = " + m_threadIndex + "]";
		}
	}

	/**
	 * Places a file named {@link #FILE_TEMP_IS_RUNNING_TESTS} in the temp folder of the execution directory and checks
	 * every {@link TIME_INTERVAL_ABORT_CHECK} seconds whether the file still exists. If the file has been deleted, the
	 * testing process will be stopped gently. <br/>
	 * <br/>
	 * This is a daemon thread by default.
	 *
	 */
	protected class AbortCheckerThread extends Thread {
		private File m_isRunningFile;

		public AbortCheckerThread() {
			this.setDaemon(true);
		}

		@Override
		public void run() {
			try {
				setUp();

				while (true) {
					sleepUntilNextCheck();

					if (isToBeAborted()) {
						abort();
						LOG.info("Abort signal received and processed.");
						return;
					}
				}
			} catch (Throwable t) {
				LOG.warn(AbortCheckerThread.class.getSimpleName() + " is inactive because of thrown exception!", t);
				return;
			}
		}

		private void setUp() throws IOException {
			String fileName = getFileInWorkingArea(FILE_TEMP_IS_RUNNING_TESTS);

			m_isRunningFile = new File(fileName);
			m_isRunningFile.createNewFile();
		}

		private void sleepUntilNextCheck() throws InterruptedException {
			Thread.sleep(TIME_INTERVAL_ABORT_CHECK * 1000);
		}

		private boolean isToBeAborted() {
			return !(m_isRunningFile.exists());
		}

		private void abort() {
			m_methodsToMutateAndTestsToRun.clear();
			m_aborted = true;
		}
	}
}
