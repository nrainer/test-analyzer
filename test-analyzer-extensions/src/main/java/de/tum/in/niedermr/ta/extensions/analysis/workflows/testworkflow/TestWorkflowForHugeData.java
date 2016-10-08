package de.tum.in.niedermr.ta.extensions.analysis.workflows.testworkflow;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.MultiFileResultReceiver;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.testworkflow.steps.MultiFileFinalizeResultStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.TestWorkflow;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * Test workflow that processes the data in multiple chunks and creates multiple output files.
 */
public class TestWorkflowForHugeData extends TestWorkflow {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(TestWorkflowForHugeData.class);

	/** {@inheritDoc} */
	@Override
	protected void setUpExecutionSteps() throws ExecutionException {
		super.setUpExecutionSteps();

		m_informationCollectorStep.setUseMultiFileOutput(true);

		// replace the finalize result step
		m_finalizeResultStep = createAndInitializeExecutionStep(MultiFileFinalizeResultStep.class);
	}

	/** {@inheritDoc} */
	@Override
	protected void afterExecution(ExecutionContext context) throws ExecutionException {
		// do not run the finalize result step again
		m_cleanupStep.start();
	}

	protected String getFileWithCollectedInformation(ExecutionContext context, int index) {
		String genericFileWithCollectedInformation = getFileInWorkingArea(context,
				EnvironmentConstants.FILE_OUTPUT_COLLECTED_INFORMATION);
		return MultiFileResultReceiver.getFileName(genericFileWithCollectedInformation, index);
	}

	/** {@inheritDoc} */
	@Override
	protected void executeCollectInformation(ExecutionContext context) throws ExecutionException {
		removeOldCollectedInformationFiles(context);
		super.executeCollectInformation(context);
	}

	/**
	 * Remove old collected information files. This is needed before creating new files because not all files may be
	 * overwritten (if the new run creates less files).
	 */
	protected void removeOldCollectedInformationFiles(ExecutionContext context) {
		for (int index = MultiFileResultReceiver.FIRST_INDEX; true; index++) {
			File file = new File(getFileWithCollectedInformation(context, index));

			if (file.exists()) {
				file.delete();
				LOGGER.info("Deleted old collected information file: " + file.getPath());
			} else {
				return;
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void loadCollectedInformationAndExecuteMutateAndTest(ExecutionContext context) throws IOException {
		int index = MultiFileResultReceiver.FIRST_INDEX;

		while (true) {
			String fileNameOfCurrentIndex = getFileWithCollectedInformation(context, index);

			if (!new File(fileNameOfCurrentIndex).exists()) {
				if (index == MultiFileResultReceiver.FIRST_INDEX) {
					LOGGER.error("No file chunks exist!");
				} else {
					LOGGER.info("No file chunk exists for index: " + index + ". Terminating.");
				}

				break;
			}

			LOGGER.info("Start test workflow for file chunk " + index);
			loadCollectedInformationAndExecuteMutateAndTest(fileNameOfCurrentIndex, index, context);
			LOGGER.info("Complete test workflow for file chunk " + index);

			index++;
		}
	}

	/**
	 * Load the collected information and execute the mutation testing.
	 * 
	 * @param currentFile
	 *            of the current chunk
	 * @param index
	 *            of the current chunk
	 * @param context
	 *            of the workflow
	 */
	protected void loadCollectedInformationAndExecuteMutateAndTest(String currentFile, int index,
			ExecutionContext context) throws IOException {
		ConcurrentLinkedQueue<TestInformation> testInformation = loadCollectedInformation(currentFile);
		executeMutateAndTest(testInformation);

		((MultiFileFinalizeResultStep) m_finalizeResultStep).setIndex(index);
		m_finalizeResultStep.start();
	}
}
