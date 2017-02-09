package de.tum.in.niedermr.ta.runner.factory;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;

/** Default factory for {@link IFactory}. */
public class DefaultFactory implements IFactory {

	/** {@inheritDoc} */
	@Override
	public ExecutionContext createNewExecutionContext(IExecutionId executionId, Configuration configuration,
			String programPath, String workingFolder) {
		return new ExecutionContext(executionId, configuration, programPath, workingFolder);
	}

	/** {@inheritDoc} */
	@Override
	public ProcessExecution createNewProcessExecution(Configuration configuration, String executionDirectory,
			String programFolderForClasspath, String workingFolderForClasspath) {
		return new ProcessExecution(executionDirectory, programFolderForClasspath, workingFolderForClasspath);
	}
}
