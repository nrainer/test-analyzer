package de.tum.in.niedermr.ta.core.execution.id;

public interface IExecutionId {

	/** Get the (short) execution id. */
	String getShortId();

	/** Create a full execution id based on this short execution id. */
	IFullExecutionId createFullExecutionId(String suffix);

	/**
	 * Get the internal id value. Returns either {@link #getShortId()} or {@link IFullExecutionId#getFullId()} (if
	 * available). Can be used if it is not important whether the short or full id should be used.
	 */
	String get();
}
