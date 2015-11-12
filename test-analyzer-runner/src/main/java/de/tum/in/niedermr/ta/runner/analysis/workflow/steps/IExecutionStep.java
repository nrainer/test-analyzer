package de.tum.in.niedermr.ta.runner.analysis.workflow.steps;

import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

public interface IExecutionStep {
	public void run() throws FailedExecution;
}
