package de.tum.in.niedermr.ta.runner.factory;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;

/**
 * Factory to create instances. <br/>
 * Note that the factory is only be used in processes that are aware of the {@link Configuration}.
 */
public interface IFactory {

	/** Create an instance of {@link ExecutionContext}. */
	ExecutionContext createNewExecutionContext(IExecutionId executionId, Configuration configuration,
			String programPath, String workingFolder);

	/** Create an instance of {@link ProcessExecution}. */
	ProcessExecution createNewProcessExecution(Configuration configuration, String executionDirectory,
			String programFolderForClasspath, String workingFolderForClasspath);
}
