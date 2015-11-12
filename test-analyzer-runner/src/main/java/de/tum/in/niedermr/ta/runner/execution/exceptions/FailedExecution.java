package de.tum.in.niedermr.ta.runner.execution.exceptions;

public class FailedExecution extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final String executionId;

	public FailedExecution(String executionId, String info) {
		super(info);
		this.executionId = executionId;
	}

	public FailedExecution(String executionId, Throwable t) {
		super(t);
		this.executionId = executionId;
	}

	public String getExecutionId() {
		return executionId;
	}
}
