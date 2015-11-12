package de.tum.in.niedermr.ta.runner.analysis.workflow;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s1.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s2.InstrumentationStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s3.InformationCollectorStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s4.MutateAndTestStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s5.FinalizeResultStep;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;
import de.tum.in.niedermr.ta.runner.execution.infocollection.CollectedInformation;

public class TestWorkflow extends AbstractWorkflow {
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

	@Override
	public void start() throws FailedExecution {
		checkIsInitialized();

		setUpExecutionSteps();

		beforeExecution();

		ConcurrentLinkedQueue<TestInformation> testInformation;

		if (m_information.getConfiguration().getExecuteCollectInformation().isTrue()) {
			testInformation = collectInformation();
		} else {
			LOG.info("Skipping steps to collect information");

			testInformation = loadExistingTestInformation();
		}

		if (m_information.getConfiguration().getExecuteMutateAndTest().isTrue()) {
			executeMutateAndTest(testInformation);
		} else {
			LOG.info("Skipping the steps to mutate and test methods");
		}

		afterExecution();
	}

	protected void checkIsInitialized() throws FailedExecution {
		if (this.m_information == null) {
			throw new IllegalStateException("Not initialized.");
		}
	}

	protected void setUpExecutionSteps() throws FailedExecution {
		m_prepareWorkingFolderStep = new PrepareWorkingFolderStep(m_information);
		m_instrumentationStep = new InstrumentationStep(m_information);
		m_informationCollectorStep = new InformationCollectorStep(m_information);
		m_mutateAndTestStep = new MutateAndTestStep(m_information);
		m_finalizeResultStep = new FinalizeResultStep(m_information);
	}

	protected void beforeExecution() throws FailedExecution {
		m_prepareWorkingFolderStep.run();
	}

	protected void afterExecution() throws FailedExecution {
		m_finalizeResultStep.run();
	}

	protected ConcurrentLinkedQueue<TestInformation> collectInformation() throws FailedExecution {
		m_instrumentationStep.run();
		m_informationCollectorStep.run();

		return m_informationCollectorStep.getMethodsToMutateAndTestsToRun();
	}

	protected ConcurrentLinkedQueue<TestInformation> loadExistingTestInformation() throws FailedExecution {
		String fileCollectedInformation = Environment.replaceWorkingFolder(
				EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION, m_information.getWorkingFolder());

		if (!new File(fileCollectedInformation).exists()) {
			throw new FailedExecution(this.m_information.getExecutionId(),
					fileCollectedInformation + " must exist in the working directory if '"
							+ m_information.getConfiguration().getExecuteCollectInformation().getName()
							+ "' is set to 'false'.");
		}

		return loadExistingTestInformationInternal();
	}

	private ConcurrentLinkedQueue<TestInformation> loadExistingTestInformationInternal() {
		ConcurrentLinkedQueue<TestInformation> testInformation = new ConcurrentLinkedQueue<>();

		try {
			List<String> data = TextFileData.readFromFile(Environment.replaceWorkingFolder(
					EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION, m_information.getWorkingFolder()));
			testInformation.addAll(CollectedInformation.parseInformationCollectorData(data));
		} catch (IOException ex) {
			LOG.fatal("When loading existing collected-information", ex);
		}

		return testInformation;
	}

	protected void executeMutateAndTest(ConcurrentLinkedQueue<TestInformation> testInformation) throws FailedExecution {
		m_mutateAndTestStep.setInputData(testInformation);

		m_mutateAndTestStep.run();
	}
}
