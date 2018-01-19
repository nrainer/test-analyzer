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

	/** Constructor. */
	public AbstractJaCoCoParser(IExecutionId executionId) {
		super(XML_SCHEMA_NAME, executionId);
	}

	protected void visitClassNodes(Document document, IResultReceiver resultReceiver, INodeVisitor visitor)
			throws XPathExpressionException {
		XPathExpression allClassesXPath = compileXPath("//class");
		NodeList classNodes = evaluateNodeList(document, allClassesXPath);
		visitNodes(classNodes, resultReceiver, visitor);
	}

	protected void visitMethodNodes(Node classNode, IResultReceiver resultReceiver, INodeVisitor visitor)
			throws XPathExpressionException {
		XPathExpression methodsOfClassXPath = compileXPath("./method");
		NodeList methodNodes = evaluateNodeList(classNode, methodsOfClassXPath);
		visitNodes(methodNodes, resultReceiver, visitor);
	}

	protected void visitNodes(NodeList nodes, IResultReceiver resultReceiver, INodeVisitor visitor)
			throws XPathExpressionException {

		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);

			visitor.visitNode(currentNode, resultReceiver);

			// performance tuning (does not influence indices in the NodeList)
			currentNode.getParentNode().removeChild(currentNode);
		}
	}

}
