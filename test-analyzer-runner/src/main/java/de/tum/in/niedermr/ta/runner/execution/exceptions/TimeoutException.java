package de.tum.in.niedermr.ta.runner.execution.exceptions;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

/** Timeout during an execution exception. */
public class TimeoutException extends ExecutionException {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Constructor. */
	public TimeoutException(IExecutionId executionId, int timeoutInSeconds) {
		super(executionId, timeoutInSeconds + " seconds");
	}
}
