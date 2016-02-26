package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s3;

import java.io.File;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

public class CleanupStep extends AbstractExecutionStep {

	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws Exception {
		if (configuration.getRemoveTempData().getValue()) {
			FileSystemUtils
					.deleteRecursively(new File(getFileInWorkingArea(EnvironmentConstants.PATH_WORKING_AREA_TEMP)));
		}
	}

	@Override
	protected String getDescription() {
		return "Performing cleanup if enabled";
	}
}
