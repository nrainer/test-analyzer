package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps.AggregatedCoverageParserStep;

/**
 * Parser for aggregated coverage information. Currently, only coverage in form
 * of XML from JaCoCo is supported.
 */
public class CoverageParserWorkflow extends AbstractCoverageParserWorkflow<AggregatedCoverageParserStep> {

	/** {@inheritDoc} */
	@Override
	protected String getOutputFile() {
		return ExtensionEnvironmentConstants.FILE_OUTPUT_COVERAGE_INFORMATION;
	}

	/** {@inheritDoc} */
	@Override
	protected Class<AggregatedCoverageParserStep> getParserStep() {
		return AggregatedCoverageParserStep.class;
	}
}
