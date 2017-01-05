package de.tum.in.niedermr.ta.runner.execution;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Execution context for tests. */
public class TestExecutionContext {

	/** Constructor. */
	private TestExecutionContext() {
		// NOP
	}

	/** Create. */
	public static ExecutionContext create() {
		return new ExecutionContext(ExecutionIdFactory.ID_FOR_TESTS, new Configuration(), "./program", "./workingArea");
	}
}
