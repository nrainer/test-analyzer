package de.tum.in.niedermr.ta.runner.execution.exceptions;

public class ExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final String m_executionId;

	public ExecutionException(String executionId, String info) {
		super(info);
		this.m_executionId = executionId;
	}

	public ExecutionException(String executionId, Throwable t) {
		super(t);
		this.m_executionId = executionId;
	}

	public String getExecutionId() {
		return m_executionId;
	}
}
