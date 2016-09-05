package de.tum.in.niedermr.ta.runner.analysis.workflow.steps;

import de.tum.in.niedermr.ta.runner.analysis.workflow.IWorkflow;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * An execution step is a part of a {@link IWorkflow}. <br/>
 * It must have a 0-parameter constructor because the instance will be created using reflection.
 */
public interface IExecutionStep {

	/** Initialize the step. */
	void initialize(ExecutionContext information);

	/** Run the step. */
	void start() throws ExecutionException;
}
