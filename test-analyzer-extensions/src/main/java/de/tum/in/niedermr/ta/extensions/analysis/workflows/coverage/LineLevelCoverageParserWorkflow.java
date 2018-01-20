package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps.LineLevelCoverageParserStep;

/**
 * Parser for line level coverage information. Currently, only coverage in form
 * of XML from JaCoCo is supported.
 */
public class LineLevelCoverageParserWorkflow extends AbstractCoverageParserWorkflow<LineLevelCoverageParserStep> {

	/** {@inheritDoc} */
	@Override
	protected String getOutputFile() {
		return ExtensionEnvironmentConstants.FILE_OUTPUT_LINE_LEVEL_COVERAGE;
	}

	/** {@inheritDoc} */
	@Override
	protected Class<LineLevelCoverageParserStep> getParserStep() {
		return LineLevelCoverageParserStep.class;
	}
}
