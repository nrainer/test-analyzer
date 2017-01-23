package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public class FinalizeResultStep extends AbstractExecutionStep {

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "FNZRES";
	}

	/** {@inheritDoc} */
	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, IOException {
		String destinationFilePath = computePathToDestinationFile(configuration);

		if (configuration.isMultiThreaded()) {
			mergeResultFiles(configuration, destinationFilePath);
		} else {
			moveResultFile(destinationFilePath);
		}
	}

	protected String computePathToDestinationFile(Configuration configuration) {
		return getFileInWorkingArea(Environment.getGenericFilePathOfOutputResult(configuration));
	}

	/** Merge the partial result files. */
	private void mergeResultFiles(Configuration configuration, final String destinationFilePath) throws IOException {
		TextFileUtility.mergeFiles(destinationFilePath, getFileInWorkingArea(FILE_TEMP_RESULT_X),
				configuration.getNumberOfThreads().getValue());
	}

	/** Copy the result file from the temp to the result folder. */
	private void moveResultFile(final String destinationFilePath) {
		File dest = new File(destinationFilePath);

		if (dest.exists()) {
			dest.delete();
		}

		File resultFile = new File(getFileInWorkingArea(getWithIndex(FILE_TEMP_RESULT_X, 0)));
		resultFile.renameTo(dest);
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Finalizing the result and removing temp files (if enabled)";
	}
}
