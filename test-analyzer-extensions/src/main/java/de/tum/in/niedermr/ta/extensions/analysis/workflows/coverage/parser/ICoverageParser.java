package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;

/** Coverage info parser. */
public interface ICoverageParser {

	/** Initialize. */
	void initialize() throws ParserConfigurationException;

	/** Parse a file. */
	void parse(File inputFile, IResultReceiver resultReceiver) throws Exception;
}
