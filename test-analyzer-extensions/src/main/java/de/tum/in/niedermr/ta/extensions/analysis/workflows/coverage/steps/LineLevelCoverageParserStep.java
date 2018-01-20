package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractParserStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.IContentParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.JaCoCoLineLevelParser;

/** Step to parse coverage files at the line level. */
public class LineLevelCoverageParserStep extends AbstractParserStep {

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "LINCOV";
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Parse line level coverage";
	}

	/** {@inheritDoc} */
	@Override
	protected IContentParser createParser(IExecutionId executionId) {
		return new JaCoCoLineLevelParser(executionId);
	}
}
