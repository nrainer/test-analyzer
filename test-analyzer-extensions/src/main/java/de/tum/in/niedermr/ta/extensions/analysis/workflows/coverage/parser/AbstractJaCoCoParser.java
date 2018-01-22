package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractXmlContentParser;

/** Coverage parser for JaCoCo XML files. */
public abstract class AbstractJaCoCoParser extends AbstractXmlContentParser {

	private static final String XML_SCHEMA_NAME = "report.dtd";

	protected static final String COUNTER_TYPE_METHOD = "METHOD";
	protected static final String COUNTER_TYPE_LINE = "LINE";
	protected static final String COUNTER_TYPE_INSTRUCTION = "INSTRUCTION";
	protected static final String COUNTER_TYPE_BRANCH = "BRANCH";

	private XPathExpression m_allClassesXPath;

	private XPathExpression m_methodsOfClassXPath;

	/** Constructor. */
	public AbstractJaCoCoParser(IExecutionId executionId) {
		super(XML_SCHEMA_NAME, executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void execCompileXPathExpressions() throws XPathExpressionException {
		super.execCompileXPathExpressions();

		m_allClassesXPath = compileXPath("//class");
		m_methodsOfClassXPath = compileXPath("./method");
	}

	protected void visitClassNodes(Document document, IResultReceiver resultReceiver, INodeVisitor visitor)
			throws XPathExpressionException {
		NodeList classNodes = evaluateNodeList(document, m_allClassesXPath);
		visitNodes(classNodes, resultReceiver, visitor);
	}

	protected void visitMethodNodes(Node classNode, IResultReceiver resultReceiver, INodeVisitor visitor)
			throws XPathExpressionException {
		NodeList methodNodes = evaluateNodeList(classNode, m_methodsOfClassXPath);
		visitNodes(methodNodes, resultReceiver, visitor);
	}
}
