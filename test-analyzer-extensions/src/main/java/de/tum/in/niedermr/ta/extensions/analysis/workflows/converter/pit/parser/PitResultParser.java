package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractXmlContentParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.result.MutationSqlOutputBuilder;

/** Coverage parser for JaCoCo XML files. */
public class PitResultParser extends AbstractXmlContentParser {

	/** Mutation node. */
	private XPathExpression m_mutationNodeXPath;
	/** Status attribute of mutation node. */
	private XPathExpression m_mutationStatusAttributeXPath;
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
		m_mutationStatusAttributeXPath = compileXPath("@status");
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
			parseMutationNode(nodeList.item(i), resultReceiver);
			resultReceiver.markResultAsPartiallyComplete();
		}
	}

	/** Parse a single mutation node. */
	private void parseMutationNode(Node mutationNode, IResultReceiver resultReceiver) throws XPathExpressionException {
		MutationSqlOutputBuilder mutationSqlOutputBuilder = getResultPresentation().createMutationSqlOutputBuilder();
		mutationSqlOutputBuilder.setMutationStatus(evaluateStringValue(mutationNode, m_mutationStatusAttributeXPath));
		mutationSqlOutputBuilder.setMutatedMethod(evaluateStringValue(mutationNode, m_mutatedClassNodeXPath),
				evaluateStringValue(mutationNode, m_mutatedMethodNodeXPath),
				evaluateStringValue(mutationNode, m_methodTypeSignatureNodeXPath));
		mutationSqlOutputBuilder.setMutatorName(evaluateStringValue(mutationNode, m_mutatorNameNodeXPath));
		mutationSqlOutputBuilder.setKillingTestSignature(evaluateStringValue(mutationNode, m_killingTestNodeXPath));
		mutationSqlOutputBuilder.setMutationDescription(evaluateStringValue(mutationNode, m_descriptionNodeXPath));
		resultReceiver.append(mutationSqlOutputBuilder.complete());
	}
}
