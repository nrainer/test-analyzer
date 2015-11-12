package de.tum.in.niedermr.ta.runner.execution.exceptions;

public class FailedExecution extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final String m_executionId;

	public FailedExecution(String executionId, String info) {
		super(info);
		this.m_executionId = executionId;
	}

	public FailedExecution(String executionId, Throwable t) {
		super(t);
		this.m_executionId = executionId;
	}

	public String getExecutionId() {
		return m_executionId;
	}
}
