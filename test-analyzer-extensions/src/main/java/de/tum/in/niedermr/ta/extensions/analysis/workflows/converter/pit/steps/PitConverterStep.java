package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.steps;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractParserStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.IContentParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser.PitResultParser;

/** Step to convert PIT result files. */
public class PitConverterStep extends AbstractParserStep {

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "CVTPIT";
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Convert PIT result files";
	}

	/** {@inheritDoc} */
	@Override
	protected IContentParser createParser(IExecutionId executionId) {
		return new PitResultParser(executionId);
	}
}
