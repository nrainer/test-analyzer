package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import java.io.File;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/** Coverage info parser. */
public interface ICoverageParser {

	/** Initialize. */
	void initialize() throws ParserConfigurationException;

	/** Parse a file. */
	void parse(File inputFile) throws Exception;

	/** Get the result. */
	List<String> getResult();

	/** Reset the result. */
	void resetResult();
}
