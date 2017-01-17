package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.TestWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class MutateAndTestStep extends AbstractExecutionStep {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(MutateAndTestStep.class);

	protected static final int TIME_INTERVAL_ABORT_CHECK = 30;

	protected ConcurrentLinkedQueue<TestInformation> m_methodsToMutateAndTestsToRun;
	protected boolean m_aborted;

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "TSTRUN";
	}

	public void setInputData(ConcurrentLinkedQueue<TestInformation> methodsToMutateAndTestsToRun) {
		this.m_methodsToMutateAndTestsToRun = methodsToMutateAndTestsToRun;
	}

	/** {@inheritDoc} */
	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws ExecutionException {
		LOGGER.info("Using " + configuration.getNumberOfThreads().getValue() + " threads for mutating and testing.");
		LOGGER.info("Candidates to be mutated: " + m_methodsToMutateAndTestsToRun.size()
				+ " methods (filters have not been applied yet)");

		startAbortChecker(configuration);

		List<MutateAndTestThread> threadList = createAndStartThreads(configuration, processExecution);

		MethodMutationTestStateStatistics aggregatedStatistics = new MethodMutationTestStateStatistics();

		for (MutateAndTestThread workerThread : threadList) {
			try {
				workerThread.join();
			} catch (InterruptedException e) {
				throw new ExecutionException(getExecutionId(), e);
			}

			aggregatedStatistics.mergeWith(workerThread.getMutationTestStatistics());
		}

		if (m_aborted) {
			LOGGER.warn("MANUALLY ABORTED.");
			throw new ExecutionException(createFullExecutionId(), "Aborted");
		} else {
			String summary = aggregatedStatistics.toSummary() + " Duration of " + getClass().getSimpleName() + " was "
					+ getDurationSinceStartInSec() + " sec. using " + threadList.size() + " threads.";
			LOGGER.info("ALL THREADS FINISHED. " + summary);
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
			LOGGER.error("When writing the summary to the file", e);
		}
	}

	/**
	 * Start the abort checker thread (if enabled).
	 * 
	 * @see TestWorkflow#CONFIGURATION_KEY_DISABLE_ABORT_CHECKER
	 */
	private void startAbortChecker(Configuration configuration) {
		m_aborted = false;

		if (configuration.getDynamicValues().getBooleanValue(TestWorkflow.CONFIGURATION_KEY_DISABLE_ABORT_CHECKER)) {
			LOGGER.info("Abort checker is disabled");
			return;
		}

		String fileName = getFileInWorkingArea(EnvironmentConstants.FILE_TEMP_IS_RUNNING_TESTS);

		AbortCheckerThread abortCheckerThread = new AbortCheckerThread(fileName, TIME_INTERVAL_ABORT_CHECK) {
			/** {@inheritDoc} */
			@Override
			protected void execAbort() {
				m_methodsToMutateAndTestsToRun.clear();
				m_aborted = true;
			}
		};

		abortCheckerThread.start();
	}

	private List<MutateAndTestThread> createAndStartThreads(Configuration configuration,
			ProcessExecution processExecution) {
		int numberOfThreads = configuration.getNumberOfThreads().getValue();
		List<MutateAndTestThread> threadList = new LinkedList<>();

		for (int threadIndex = 0; threadIndex < numberOfThreads; threadIndex++) {
			MutateAndTestThread mutateAndTestThread = new MutateAndTestThread(threadIndex, getContext(),
					processExecution, m_methodsToMutateAndTestsToRun, getSuffixForFullExecutionId());

			threadList.add(mutateAndTestThread);
			mutateAndTestThread.start();
		}

		return threadList;
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Mutating methods and running testcases";
	}
}
