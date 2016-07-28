package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s1;

import java.io.File;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

public class PrepareWorkingFolderStep extends AbstractExecutionStep {

	@Override
	protected String getSuffixForFullExecutionId() {
		return "PREFOL";
	}

	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws Exception {
		final File workingAreaTemp = new File(getFileInWorkingArea(EnvironmentConstants.PATH_WORKING_AREA_TEMP));
		final File workingAreaResult = new File(getFileInWorkingArea(EnvironmentConstants.PATH_WORKING_AREA_RESULT));

		if (workingAreaTemp.exists()) {
			FileSystemUtils.deleteRecursively(workingAreaTemp);
		}

		FileSystemUtils.ensureDirectoryExists(workingAreaTemp);
		FileSystemUtils.ensureDirectoryExists(workingAreaResult);
	}

	@Override
	protected String getDescription() {
		return "Preparing the working folder";
	}
}
