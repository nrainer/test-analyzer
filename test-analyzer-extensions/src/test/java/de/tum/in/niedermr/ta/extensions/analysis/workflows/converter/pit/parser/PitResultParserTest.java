package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractContentParserTest;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.IContentParser;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link PitResultParser}. */
public class PitResultParserTest extends AbstractContentParserTest {

	/** Constructor. */
	public PitResultParserTest() {
		super("mutations.xml", "expected.sql.txt");
	}

	/** {@inheritDoc} */
	@Override
	protected IContentParser createParser() {
		return new PitResultParser(ExecutionIdFactory.ID_FOR_TESTS);
	}
}
