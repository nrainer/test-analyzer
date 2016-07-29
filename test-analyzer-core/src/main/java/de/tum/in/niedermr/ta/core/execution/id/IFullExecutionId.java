package de.tum.in.niedermr.ta.core.execution.id;

public interface IFullExecutionId extends IExecutionId {

	/** Get the full execution id. */
	String getFullId();

	/** Convert the id to a short execution id. */
	IExecutionId convertToShortExecutionId();
}
