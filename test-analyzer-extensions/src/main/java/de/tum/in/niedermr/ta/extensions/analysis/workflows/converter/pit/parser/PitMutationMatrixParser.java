package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;

/** Coverage parser for modified PIT XML files that contain data to create a mutation matrix. */
public class PitMutationMatrixParser extends PitResultParser {

	private String m_testcaseUnrollingSeparator;

	/** Constructor. */
	public PitMutationMatrixParser(IExecutionId executionId, String testcaseUnrollingSeparator) {
		super(executionId);
		m_testcaseUnrollingSeparator = testcaseUnrollingSeparator;
	}
}
