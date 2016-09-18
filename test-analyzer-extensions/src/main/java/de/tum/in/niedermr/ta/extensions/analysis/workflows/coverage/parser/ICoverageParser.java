package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import java.io.File;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;

/** Coverage info parser. */
public interface ICoverageParser {

	/** Initialize. */
	void initialize() throws CoverageParserException;

	/** Parse a file. */
	void parse(File inputFile, IResultReceiver resultReceiver) throws CoverageParserException;
}
