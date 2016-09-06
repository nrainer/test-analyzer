package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import java.util.List;

/** Result receiver. */
public interface IResultReceiver {

	/** Append a string line to the result. */
	void append(String line);

	/** Append multiple string lines to the result. */
	void append(List<String> lines);

	/** Mark the result as complete. */
	void markResultAsComplete();
}
