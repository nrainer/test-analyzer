package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import org.junit.Test;

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

	/** Test. */
	@Test
	public void testParserWithUnrolling() throws Exception {
		PitResultParser parser = createParser();
		parser.enableTestcaseUnrolling("|");
		testParser(parser, "mutations-2.xml", "expected-2.sql.txt");
	}
}
