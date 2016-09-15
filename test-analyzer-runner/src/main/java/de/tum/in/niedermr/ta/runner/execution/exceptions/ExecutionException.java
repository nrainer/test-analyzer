package de.tum.in.niedermr.ta.runner.execution.exceptions;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

public class ExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final IExecutionId m_executionId;

	public ExecutionException(IExecutionId executionId, String message) {
		super(message);
		this.m_executionId = executionId;
	}

	public ExecutionException(IExecutionId executionId, Throwable t) {
		super(t);
		this.m_executionId = executionId;
	}

	public ExecutionException(String message, Throwable t) {
		super(message, t);
		m_executionId = null;
	}

	/** Get the execution id. May return null. */
	public IExecutionId getExecutionId() {
		return m_executionId;
	}
}
