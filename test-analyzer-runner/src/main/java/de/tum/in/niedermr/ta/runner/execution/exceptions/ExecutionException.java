package de.tum.in.niedermr.ta.runner.execution.exceptions;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

public class ExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final IExecutionId m_executionId;

	public ExecutionException(IExecutionId executionId, String info) {
		super(info);
		this.m_executionId = executionId;
	}

	public ExecutionException(IExecutionId executionId, Throwable t) {
		super(t);
		this.m_executionId = executionId;
	}

	public IExecutionId getExecutionId() {
		return m_executionId;
	}
}
