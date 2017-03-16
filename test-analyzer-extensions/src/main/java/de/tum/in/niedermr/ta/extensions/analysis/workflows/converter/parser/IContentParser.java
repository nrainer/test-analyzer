package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser;

import java.io.File;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;

/** Content parser. */
public interface IContentParser {

	/** Initialize. */
	void initialize() throws ContentParserException;

	/** Parse a file. */
	void parse(File inputFile, IResultReceiver resultReceiver) throws ContentParserException;
}
