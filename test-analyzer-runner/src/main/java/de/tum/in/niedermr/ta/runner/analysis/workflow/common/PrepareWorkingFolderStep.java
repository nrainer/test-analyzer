package de.tum.in.niedermr.ta.runner.analysis.workflow.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * This step prepares the working folder. It should be executed before each workflow to ensure that the temp folder is
 * empty.
 */
public class PrepareWorkingFolderStep extends AbstractExecutionStep {

	private final List<File> m_foldersToBeCreated = new ArrayList<>();

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "PREFOL";
	}

	/** {@inheritDoc} */
	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, IOException {
		File workingAreaTemp = new File(getFileInWorkingArea(EnvironmentConstants.PATH_WORKING_AREA_TEMP));
		File workingAreaResult = new File(getFileInWorkingArea(EnvironmentConstants.PATH_WORKING_AREA_RESULT));

		addFolderToBeCreated(workingAreaTemp);
		addFolderToBeCreated(workingAreaResult);

		deleteTemporaryFolder(workingAreaTemp);
		createFolders();
	}

	private void deleteTemporaryFolder(File workingAreaTemp) {
		if (workingAreaTemp.exists()) {
			FileSystemUtils.deleteRecursively(workingAreaTemp);
		}
	}

	private void createFolders() throws IOException {
		for (File folder : m_foldersToBeCreated) {
			FileSystemUtils.ensureDirectoryExists(folder);
		}
	}

	/** Add a folder that should be created if it does not exist yet. */
	public void addFolderToBeCreated(File folder) {
		m_foldersToBeCreated.add(folder);
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Preparing the working folder";
	}
}
