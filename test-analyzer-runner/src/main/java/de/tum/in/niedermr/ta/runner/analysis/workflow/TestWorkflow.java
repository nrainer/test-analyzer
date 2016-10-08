package de.tum.in.niedermr.ta.runner.analysis.workflow;

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
	public static final DynamicConfigurationKey CONFIGURATION_KEY_EXECUTE_COLLECT_INFORMATION = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.ADVANCED, "testworkflow.collectInformation", true);

	/**
	 * <code>advanced.testworkflow.mutateAndTest</code>: Allows skipping the mutation testing steps. <br/>
	 * If false, {@link MutateAndTestStep} will be skipped.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_EXECUTE_MUTATE_AND_TEST = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.ADVANCED, "testworkflow.mutateAndTest", true);

	/**
	 * <code>advanced.testworkflow.abortchecker.disabled</code>: Disables the abort checker.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_DISABLE_ABORT_CHECKER = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.ADVANCED, "testworkflow.abortchecker.disabled", false);

	protected PrepareWorkingFolderStep m_prepareWorkingFolderStep;
	protected InstrumentationStep m_instrumentationStep;
	protected InformationCollectorStep m_informationCollectorStep;
	protected MutateAndTestStep m_mutateAndTestStep;
	protected FinalizeResultStep m_finalizeResultStep;
	protected CleanupStep m_cleanupStep;

	/** Default constructor for reflective instantiation. */
	public TestWorkflow() {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		setUpExecutionSteps();
		beforeExecution(context);

		try {
			executeCoreWorkflow(context, configuration);
		} catch (IOException e) {
			throw new ExecutionException(context.getExecutionId(), e);
		}

		afterExecution(context);
	}

	/** Execute the main workflow logic. */
	protected void executeCoreWorkflow(ExecutionContext context, Configuration configuration)
			throws ExecutionException, IOException {
		if (configuration.getDynamicValues().getBooleanValue(CONFIGURATION_KEY_EXECUTE_COLLECT_INFORMATION)) {
			executeCollectInformation(context);
		} else {
			LOGGER.info("Skipping steps to collect information");
		}

		if (configuration.getDynamicValues().getBooleanValue(CONFIGURATION_KEY_EXECUTE_MUTATE_AND_TEST)) {
			loadCollectedInformationAndExecuteMutateAndTest(context);
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
		if (context.getConfiguration().getDynamicValues().getBooleanValue(CONFIGURATION_KEY_EXECUTE_MUTATE_AND_TEST)) {
			m_finalizeResultStep.start();
		}

		m_cleanupStep.start();
	}

	/**
	 * Executed the collect information steps.
	 * 
	 * @param context
	 *            of the workflow
	 */
	protected void executeCollectInformation(ExecutionContext context) throws ExecutionException {
		m_instrumentationStep.start();
		m_informationCollectorStep.start();
	}

	protected void loadCollectedInformationAndExecuteMutateAndTest(ExecutionContext context) throws IOException {
		String fileWithCollectedInformation = getFileInWorkingArea(context,
				EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION);
		ConcurrentLinkedQueue<TestInformation> testInformation = loadCollectedInformation(fileWithCollectedInformation);
		executeMutateAndTest(testInformation);
	}

	protected ConcurrentLinkedQueue<TestInformation> loadCollectedInformation(String fileWithCollectedInformation)
			throws ExecutionException, IOException {
		List<String> rawData = TextFileData.readFromFile(fileWithCollectedInformation);

		ConcurrentLinkedQueue<TestInformation> testInformation = new ConcurrentLinkedQueue<>();
		testInformation.addAll(CollectedInformationUtility.parseMethodTestcaseText(rawData));
		return testInformation;
	}

	protected void executeMutateAndTest(ConcurrentLinkedQueue<TestInformation> testInformation)
			throws ExecutionException {
		m_mutateAndTestStep.setInputData(testInformation);
		m_mutateAndTestStep.start();
	}
}
