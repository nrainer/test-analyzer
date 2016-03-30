package de.tum.in.niedermr.ta.runner.execution.exceptions;

public class ProcessExecutionFailedException extends ExecutionException {

	private static final long serialVersionUID = 1L;

	public ProcessExecutionFailedException(String executionId, String message) {
		super(executionId, message);
	}
}
