package de.tum.in.niedermr.ta.runner.execution.exceptions;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

/** Process execution failed exception. */
public class ProcessExecutionFailedException extends ExecutionException {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Constructor. */
	public ProcessExecutionFailedException(IExecutionId executionId, String message) {
		super(executionId, message);
	}
}
