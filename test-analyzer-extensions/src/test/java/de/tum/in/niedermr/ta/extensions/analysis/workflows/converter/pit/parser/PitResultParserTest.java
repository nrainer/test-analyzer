package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractContentParserTest;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link PitResultParser}. */
public class PitResultParserTest extends AbstractContentParserTest {

	/** Constructor. */
	public PitResultParserTest() {
		super("mutations-1.xml", "expected-1.sql.txt");
	}

	/** {@inheritDoc} */
	@Override
	protected PitResultParser createParser() {
		return new PitResultParser(ExecutionIdFactory.ID_FOR_TESTS);
	}
}
