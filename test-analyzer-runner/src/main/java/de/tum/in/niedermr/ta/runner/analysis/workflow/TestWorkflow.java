package de.tum.in.niedermr.ta.runner.analysis.workflow;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.preparation.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s2.InstrumentationStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s3.InformationCollectorStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s4.MutateAndTestStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s5.FinalizeResultStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.infocollection.CollectedInformation;

public class TestWorkflow extends AbstractWorkflow {
	private static final Logger LOG = LogManager.getLogger(TestWorkflow.class);

	protected PrepareWorkingFolderStep m_prepareWorkingFolderStep;
	protected InstrumentationStep m_instrumentationStep;
	protected InformationCollectorStep m_informationCollectorStep;
	protected MutateAndTestStep m_mutateAndTestStep;
	protected FinalizeResultStep m_finalizeResultStep;

	/**
	 * Default constructor for reflective instantiation.
	 */
	public TestWorkflow() {
	}

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		setUpExecutionSteps();

		beforeExecution();

		ConcurrentLinkedQueue<TestInformation> testInformation;

		if (configuration.getExecuteCollectInformation().isTrue()) {
			testInformation = collectInformation();
		} else {
			LOG.info("Skipping steps to collect information");

			testInformation = loadExistingTestInformation(context, configuration);
		}

		if (configuration.getExecuteMutateAndTest().isTrue()) {
			executeMutateAndTest(testInformation);
		} else {
			LOG.info("Skipping the steps to mutate and test methods");
		}

		afterExecution();
	}

	protected void setUpExecutionSteps() throws ExecutionException {
		m_prepareWorkingFolderStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		m_instrumentationStep = createAndInitializeExecutionStep(InstrumentationStep.class);
		m_informationCollectorStep = createAndInitializeExecutionStep(InformationCollectorStep.class);
		m_mutateAndTestStep = createAndInitializeExecutionStep(MutateAndTestStep.class);
		m_finalizeResultStep = createAndInitializeExecutionStep(FinalizeResultStep.class);
	}

	protected void beforeExecution() throws ExecutionException {
		m_prepareWorkingFolderStep.run();
	}

	protected void afterExecution() throws ExecutionException {
		m_finalizeResultStep.run();
	}

	protected ConcurrentLinkedQueue<TestInformation> collectInformation() throws ExecutionException {
		m_instrumentationStep.run();
		m_informationCollectorStep.run();

		return m_informationCollectorStep.getMethodsToMutateAndTestsToRun();
	}

	protected ConcurrentLinkedQueue<TestInformation> loadExistingTestInformation(ExecutionContext context,
			Configuration configuration) throws ExecutionException {
		String workingFolder = context.getWorkingFolder();
		String fileCollectedInformation = Environment
				.replaceWorkingFolder(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION, workingFolder);

		if (!new File(fileCollectedInformation).exists()) {
			throw new ExecutionException(context.getExecutionId(),
					fileCollectedInformation + " must exist in the working directory if '"
							+ configuration.getExecuteCollectInformation().getName() + "' is set to 'false'.");
		}

		return loadExistingTestInformationInternal(workingFolder);
	}

	private ConcurrentLinkedQueue<TestInformation> loadExistingTestInformationInternal(String workingFolder) {
		ConcurrentLinkedQueue<TestInformation> testInformation = new ConcurrentLinkedQueue<>();

		try {
			List<String> data = TextFileData.readFromFile(Environment
					.replaceWorkingFolder(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION, workingFolder));
			testInformation.addAll(CollectedInformation.parseInformationCollectorData(data));
		} catch (IOException ex) {
			LOG.fatal("When loading existing collected-information", ex);
		}

		return testInformation;
	}

	protected void executeMutateAndTest(ConcurrentLinkedQueue<TestInformation> testInformation)
			throws ExecutionException {
		m_mutateAndTestStep.setInputData(testInformation);
		m_mutateAndTestStep.run();
	}
}
