package de.tum.in.niedermr.ta.runner.analysis.workflow;

import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * This class will be instantiated using reflection. Thus, it must have a standard constructor.
 *
 */
public interface IWorkflow {
	public void start() throws ExecutionException;

	public void initWorkflow(ExecutionContext executionContext);

	public String getName();
}
