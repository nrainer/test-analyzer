package de.tum.in.niedermr.ta.runner.execution.id;

import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;

/** Factory to create instances of {@link IExecutionId} and {@link IFullExecutionId}. */
public class ExecutionIdFactory {

	/** Length of the short execution id. */
	private static final int SHORT_EXECUTION_ID_DEFAULT_LENGTH = 4;

	/** Execution id that has not been set yet. */
	public static final IExecutionId NOT_SPECIFIED = parseShortExecutionId("****");

	/** Constructor. */
	private ExecutionIdFactory() {
		// NOP
	}

	/** Create a new instance with a random value. */
	public static IExecutionId createNewShortExecutionId() {
		return parseShortExecutionId(CommonUtility.createRandomId(SHORT_EXECUTION_ID_DEFAULT_LENGTH));
	}

	/** Parse a short execution id. */
	public static IExecutionId parseShortExecutionId(String executionId) {
		return ExecutionId.parseShortExecutionId(executionId);
	}

	/**
	 * Parse a short execution id.
	 * 
	 * @param allowTrimming
	 *            if true, a full execution id will be trimmed to a short id, otherwise an exception will be thrown
	 */
	public static IExecutionId parseShortExecutionId(String executionId, boolean allowTrimming) {
		return ExecutionId.parseShortExecutionId(executionId, allowTrimming);
	}

	/** Parse a full execution id. */
	public static IFullExecutionId parseFullExecutionId(String value) {
		return ExecutionId.parseFullExecutionId(value);
	}
}
