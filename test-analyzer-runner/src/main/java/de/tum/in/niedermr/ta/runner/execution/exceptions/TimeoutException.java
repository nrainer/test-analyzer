package de.tum.in.niedermr.ta.runner.execution.exceptions;

import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;

public class TimeoutException extends FailedExecution {
	private static final long serialVersionUID = 1L;

	public TimeoutException(String executionId, int timeoutInSeconds) {
		super(executionId, LoggingUtil.appendPluralS(timeoutInSeconds, "second", true));
	}
}
