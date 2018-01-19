package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractXmlContentParser;

/** Coverage parser for JaCoCo XML files. */
public abstract class AbstractJaCoCoParser extends AbstractXmlContentParser {

	private static final String XML_SCHEMA_NAME = "report.dtd";

	protected static final String COUNTER_TYPE_METHOD = "METHOD";
	protected static final String COUNTER_TYPE_LINE = "LINE";
	protected static final String COUNTER_TYPE_INSTRUCTION = "INSTRUCTION";
	protected static final String COUNTER_TYPE_BRANCH = "BRANCH";

	/** Constructor. */
	public AbstractJaCoCoParser(IExecutionId executionId) {
		super(XML_SCHEMA_NAME, executionId);
	}
}
