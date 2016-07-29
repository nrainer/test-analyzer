package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s3;

import java.io.File;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

public class TearDownStep extends AbstractExecutionStep {

	@Override
	protected String getSuffixForFullExecutionId() {
		return "TEARDOWN";
	}

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
