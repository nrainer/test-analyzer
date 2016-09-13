package de.tum.in.niedermr.ta.runner.execution.exceptions;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

public class TimeoutException extends ExecutionException {
	private static final long serialVersionUID = 1L;

	public TimeoutException(IExecutionId executionId, int timeoutInSeconds) {
		super(executionId, timeoutInSeconds + " seconds");
	}
}
