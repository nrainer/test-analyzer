package de.tum.in.niedermr.ta.runner.analysis.workflow;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.CleanupStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow.FinalizeResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow.InformationCollectorStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow.InstrumentationStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow.MutateAndTestStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.infocollection.CollectedInformationUtility;

/** Main workflow for the mutation test analysis. */
public class TestWorkflow extends AbstractWorkflow {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(TestWorkflow.class);

	/**
	 * <code>advanced.testworkflow.collectInformation</code>: Allows skipping the information collections steps
	 * (default: collect information = true). <br/>
	 * If false, {@link InstrumentationStep} and {@link InformationCollectorStep} will be skipped and
	 * {@link de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants#FILE_OUTPUT_COLLECTED_INFORMATION}
	 * will be loaded instead from the working folder.<br/>
	 */
	private static final DynamicConfigurationKey EXECUTE_COLLECT_INFORMATION_KEY = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.ADVANCED, "testworkflow.collectInformation", true);
	/**
	 * <code>advanced.testworkflow.mutateAndTest</code>: Allows skipping the mutation testing steps. <br/>
	 * If false, {@link MutateAndTestStep} will be skipped.
	 */
	private static final DynamicConfigurationKey EXECUTE_MUTATE_AND_TEST_KEY = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.ADVANCED, "testworkflow.mutateAndTest", true);

	protected PrepareWorkingFolderStep m_prepareWorkingFolderStep;
	protected InstrumentationStep m_instrumentationStep;
	protected InformationCollectorStep m_informationCollectorStep;
	protected MutateAndTestStep m_mutateAndTestStep;
	protected FinalizeResultStep m_finalizeResultStep;
	protected CleanupStep m_cleanupStep;

	/** Default constructor for reflective instantiation. */
	public TestWorkflow() {
	}

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		setUpExecutionSteps();

		beforeExecution(context);

		executeCoreWorkflow(context, configuration);

		afterExecution(context);
	}

	/** Execute the main workflow logic. */
	protected void executeCoreWorkflow(ExecutionContext context, Configuration configuration) {
		ConcurrentLinkedQueue<TestInformation> testInformation = collectInformationOrLoadFromFile(context,
				configuration);
		executeMutationTestsIfEnabled(configuration, testInformation);
	}

	/**
	 * Collect the information needed to execute teh mutation tests (if enabled in the configuration) or load the data
	 * from a file.
	 */
	protected ConcurrentLinkedQueue<TestInformation> collectInformationOrLoadFromFile(ExecutionContext context,
			Configuration configuration) {
		if (configuration.getDynamicValues().getBooleanValue(EXECUTE_COLLECT_INFORMATION_KEY)) {
			return collectInformation();
		} else {
			LOGGER.info("Skipping steps to collect information");
			return loadExistingTestInformation(context);
		}
	}

	/** Execute the mutation tests (if enabled in the configuration). */
	protected void executeMutationTestsIfEnabled(Configuration configuration,
			ConcurrentLinkedQueue<TestInformation> testInformation) {
		if (configuration.getDynamicValues().getBooleanValue(EXECUTE_MUTATE_AND_TEST_KEY)) {
			executeMutateAndTest(testInformation);
		} else {
			LOGGER.info("Skipping the steps to mutate and test methods");
		}
	}

	protected void setUpExecutionSteps() throws ExecutionException {
		m_prepareWorkingFolderStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		m_instrumentationStep = createAndInitializeExecutionStep(InstrumentationStep.class);
		m_informationCollectorStep = createAndInitializeExecutionStep(InformationCollectorStep.class);
		m_mutateAndTestStep = createAndInitializeExecutionStep(MutateAndTestStep.class);
		m_finalizeResultStep = createAndInitializeExecutionStep(FinalizeResultStep.class);
		m_cleanupStep = createAndInitializeExecutionStep(CleanupStep.class);
	}

	/**
	 * Executed before the execution of the main logic.
	 * 
	 * @param context
	 *            of the workflow
	 */
	protected void beforeExecution(ExecutionContext context) throws ExecutionException {
		m_prepareWorkingFolderStep.start();
	}

	/**
	 * Executed after the execution of the main logic.
	 * 
	 * @param context
	 *            of the workflow
	 */
	protected void afterExecution(ExecutionContext context) throws ExecutionException {
		if (context.getConfiguration().getDynamicValues().getBooleanValue(EXECUTE_MUTATE_AND_TEST_KEY)) {
			m_finalizeResultStep.start();
		}

		m_cleanupStep.start();
	}

	protected ConcurrentLinkedQueue<TestInformation> collectInformation() throws ExecutionException {
		m_instrumentationStep.start();
		m_informationCollectorStep.start();

		return m_informationCollectorStep.getMethodsToMutateAndTestsToRun();
	}

	protected ConcurrentLinkedQueue<TestInformation> loadExistingTestInformation(ExecutionContext context)
			throws ExecutionException {
		String workingFolder = context.getWorkingFolder();
		String fileCollectedInformation = Environment
				.replaceWorkingFolder(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION, workingFolder);

		if (!new File(fileCollectedInformation).exists()) {
			throw new ExecutionException(context.getExecutionId(),
					fileCollectedInformation + " must exist in the working directory if '"
							+ EXECUTE_COLLECT_INFORMATION_KEY.getName() + "' is set to 'false'.");
		}

		return loadExistingTestInformationInternal(workingFolder);
	}

	protected ConcurrentLinkedQueue<TestInformation> loadExistingTestInformationInternal(String workingFolder) {
		ConcurrentLinkedQueue<TestInformation> testInformation = new ConcurrentLinkedQueue<>();

		try {
			List<String> data = TextFileData.readFromFile(Environment
					.replaceWorkingFolder(EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION, workingFolder));
			testInformation.addAll(CollectedInformationUtility.parseMethodTestcaseText(data));
		} catch (IOException ex) {
			LOGGER.fatal("When loading existing collected-information", ex);
		}

		return testInformation;
	}

	protected void executeMutateAndTest(ConcurrentLinkedQueue<TestInformation> testInformation)
			throws ExecutionException {
		m_mutateAndTestStep.setInputData(testInformation);
		m_mutateAndTestStep.start();
	}
}
