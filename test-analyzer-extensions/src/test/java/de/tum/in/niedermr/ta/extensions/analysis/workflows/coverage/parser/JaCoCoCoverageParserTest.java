package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractContentParserTest;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.IContentParser;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link JaCoCoCoverageParser}. */
public class JaCoCoCoverageParserTest extends AbstractContentParserTest {

	/** Constructor. */
	public JaCoCoCoverageParserTest() {
		super("coverage.xml", "expected.sql.txt");
	}

	/** {@inheritDoc} */
	@Override
	protected IContentParser createParser() {
		return new JaCoCoCoverageParser(ExecutionIdFactory.ID_FOR_TESTS);
	}
}
