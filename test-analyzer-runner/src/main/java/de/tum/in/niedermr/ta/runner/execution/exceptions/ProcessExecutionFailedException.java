package de.tum.in.niedermr.ta.runner.execution.exceptions;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

public class ProcessExecutionFailedException extends ExecutionException {

	private static final long serialVersionUID = 1L;

	public ProcessExecutionFailedException(IExecutionId executionId, String message) {
		super(executionId, message);
	}
}
