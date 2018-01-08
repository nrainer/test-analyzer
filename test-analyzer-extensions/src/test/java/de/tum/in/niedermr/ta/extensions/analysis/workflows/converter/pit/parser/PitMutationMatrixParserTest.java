package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractContentParserTest;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link PitMutationMatrixParser}. */
public class PitMutationMatrixParserTest extends AbstractContentParserTest {

	/** Constructor. */
	public PitMutationMatrixParserTest() {
		super("mutations-1.xml", "expected-1.sql.txt");
	}

	/** {@inheritDoc} */
	@Override
	protected PitResultParser createParser() {
		return new PitMutationMatrixParser(ExecutionIdFactory.ID_FOR_TESTS, "|");
	}
}
