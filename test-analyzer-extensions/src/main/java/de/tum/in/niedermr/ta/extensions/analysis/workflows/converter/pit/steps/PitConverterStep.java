package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.steps;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractParserStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.IContentParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser.PitMutationMatrixParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser.PitResultParser;

/** Step to convert PIT result files. */
public class PitConverterStep extends AbstractParserStep {

	/**
	 * Use data from an execution of a modified version of PIT which writes all killing and succeeding test cases for
	 * each mutation into the output. Otherwise, the data only contains the first killing test case (if the mutation was
	 * killed).
	 */
	private boolean m_parseMutationMatrix = false;
	private String m_testcaseSeparatorForUnrolling;

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
		if (m_parseMutationMatrix) {
			return new PitMutationMatrixParser(executionId, m_testcaseSeparatorForUnrolling);
		}

		return new PitResultParser(executionId);
	}

	public void enableParseMutationMatrix(String testcaseSeparator) {
		m_parseMutationMatrix = true;
		m_testcaseSeparatorForUnrolling = testcaseSeparator;
	}
}
