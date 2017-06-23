package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractXmlContentParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.result.MutationSqlOutputBuilder;

/** Coverage parser for JaCoCo XML files. */
public class PitResultParser extends AbstractXmlContentParser {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(PitResultParser.class);

	/** Mutation node. */
	private XPathExpression m_mutationNodeXPath;
	/** Class node of mutation node. */
	private XPathExpression m_mutatedClassNodeXPath;
	/** Method node of mutation node. */
	private XPathExpression m_mutatedMethodNodeXPath;
	/** Description node of mutation node. */
	private XPathExpression m_methodTypeSignatureNodeXPath;
	/** Mutator node of mutation node. */
	private XPathExpression m_mutatorNameNodeXPath;
	/** Killing test node of mutation node. */
	private XPathExpression m_killingTestNodeXPath;
	/** Description node of mutation node. */
	private XPathExpression m_descriptionNodeXPath;

	/** Constructor. */
	public PitResultParser(IExecutionId executionId) {
		super("", executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		initializeXPathExpressions();

		parseMutationNodes(document, resultReceiver);
		resultReceiver.markResultAsComplete();
	}

	/** Parse a single mutation node. */
	private void initializeXPathExpressions() throws XPathExpressionException {
		m_mutationNodeXPath = compileXPath("mutations/mutation");
		m_mutatedClassNodeXPath = compileXPath("./mutatedClass");
		m_mutatedMethodNodeXPath = compileXPath("./mutatedMethod");
		m_methodTypeSignatureNodeXPath = compileXPath("./methodDescription");
		m_mutatorNameNodeXPath = compileXPath("./mutator");
		m_killingTestNodeXPath = compileXPath("./killingTest");
		m_descriptionNodeXPath = compileXPath("./description");
	}

	/** Parse the mutation nodes. */
	private void parseMutationNodes(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		NodeList nodeList = evaluateNodeList(document, m_mutationNodeXPath);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);

			parseMutationNode(currentNode, resultReceiver);
			resultReceiver.markResultAsPartiallyComplete();

			if (i > 0 && i % 1000 == 0) {
				LOGGER.info("Parsed mutation node number " + i + ".");
			}

			// remove the current node from the parent node to improve the performance; this does not influence the
			// indices in the NodeList
			currentNode.getParentNode().removeChild(currentNode);
		}
	}

	/** Parse a single mutation node. */
	private void parseMutationNode(Node mutationNode, IResultReceiver resultReceiver) throws XPathExpressionException {
		MutationSqlOutputBuilder mutationSqlOutputBuilder = getResultPresentation().createMutationSqlOutputBuilder();
		mutationSqlOutputBuilder.setMutationStatus(evaluateAttributeValue(mutationNode, "status"));
		mutationSqlOutputBuilder.setMutatedMethod(evaluateStringValue(mutationNode, m_mutatedClassNodeXPath),
				evaluateStringValue(mutationNode, m_mutatedMethodNodeXPath),
				evaluateStringValue(mutationNode, m_methodTypeSignatureNodeXPath));
		mutationSqlOutputBuilder.setMutatorName(evaluateStringValue(mutationNode, m_mutatorNameNodeXPath));
		mutationSqlOutputBuilder.setKillingTestSignature(evaluateStringValue(mutationNode, m_killingTestNodeXPath));
		mutationSqlOutputBuilder.setMutationDescription(evaluateStringValue(mutationNode, m_descriptionNodeXPath));
		resultReceiver.append(mutationSqlOutputBuilder.complete());
	}
}