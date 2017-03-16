package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps;

import java.io.File;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.AbstractParserStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.CoverageParserException;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.ICoverageParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.JaCoCoXmlParser;

/** Step to parse coverage files. */
public class CoverageParserStep extends AbstractParserStep {

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "COVPAR";
	}

	/** {@inheritDoc} */
	@Override
	protected void parse(File inputFile, IResultReceiver resultReceiver) throws CoverageParserException {
		ICoverageParser coverageParser = new JaCoCoXmlParser(getExecutionId());
		coverageParser.initialize();
		coverageParser.parse(inputFile, resultReceiver);
		resultReceiver.markResultAsComplete();
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Parse coverage files";
	}
}
