package de.tum.in.niedermr.ta.runner.analysis.workflow;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

/**
 * This class will be instantiated using reflection. Thus, it must have a standard constructor.
 *
 */
public interface IWorkflow {
	public void start() throws FailedExecution;

	public void init(String execId, Configuration configuration, String programPath, String workingFolder);

	public String getName();
}
