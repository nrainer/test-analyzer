package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import java.util.List;

/** Result receiver. */
public interface IResultReceiver {

	/** Append a string line to the result. */
	void append(String line);

	/** Append multiple string lines to the result. */
	void append(List<String> lines);

	/**
	 * Mark the result as partially complete allowing to split the results at this point.
	 */
	void markResultAsPartiallyComplete();

	/**
	 * Mark the result as complete.<br/>
	 * It is important to eventually invoke this method (otherwise the result might not get flushed)! (May be invoked
	 * multiple times.)
	 */
	void markResultAsComplete();
}
