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
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.INodeVisitor;

/** Coverage parser for PIT XML files. */
public class PitResultParser extends AbstractXmlContentParser {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(PitResultParser.class);

	private static final String STACK_DISTANCE_RECORDING_CALL_PACKAGE = "de/tum/in/niedermr/ta/extensions/analysis/workflows/stackdistance/recording";

	private static final String STATUS_NO_COVERAGE = "NO_COVERAGE";

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
	protected XPathExpression m_killingTestNodeXPath;
	/** Index node of mutation node. */
	private XPathExpression m_indexNodeXPath;
	/** Line number node of mutation node. */
	private XPathExpression m_lineNumberNodeXPath;
	/** Description node of mutation node. */
	private XPathExpression m_descriptionNodeXPath;
	/** Status attribute. */
	private String m_statusAttributeName;

	/** Skip mutations with status {@value #STATUS_NO_COVERAGE}. */
	private boolean m_skipNoCoverageMutations = true;

	/** Constructor. */
	public PitResultParser(IExecutionId executionId) {
		super("", executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		parseMutationNodes(document, resultReceiver);
		resultReceiver.markResultAsComplete();
	}

	/** {@inheritDoc} */
	@Override
	protected void execCompileXPathExpressions() throws XPathExpressionException {
		super.execCompileXPathExpressions();

		m_mutationNodeXPath = compileXPath("mutations/mutation");
		m_mutatedClassNodeXPath = compileXPath("./mutatedClass");
		m_mutatedMethodNodeXPath = compileXPath("./mutatedMethod");
		m_methodTypeSignatureNodeXPath = compileXPath("./methodDescription");
		m_mutatorNameNodeXPath = compileXPath("./mutator");
		m_killingTestNodeXPath = compileXPath("./killingTest");
		m_indexNodeXPath = compileXPath("./index");
		m_lineNumberNodeXPath = compileXPath("./lineNumber");
		m_descriptionNodeXPath = compileXPath("./description");
		m_statusAttributeName = "status";
	}

	/** Parse the mutation nodes. */
	private void parseMutationNodes(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		NodeList nodeList = evaluateNodeList(document, m_mutationNodeXPath);

		visitNodes(nodeList, new INodeVisitor() {

			/** {@inheritDoc} */
			@Override
			public void visitNode(Node currentNode, int nodeIndex) throws XPathExpressionException {
				if (isAcceptedMutationNode(currentNode)) {
					parseMutationNodeAndAppendToResultReceiver(currentNode, nodeIndex, resultReceiver);
					resultReceiver.markResultAsPartiallyComplete();
				} else {
					LOGGER.debug("Skipping mutation node " + nodeIndex + ".");
				}

				if (nodeIndex > 0 && nodeIndex % 100 == 0) {
					LOGGER.info("Parsed mutation node number " + nodeIndex + ".");
				}
			}
		});
	}

	protected boolean isAcceptedMutationNode(Node mutationNode) throws XPathExpressionException {
		String mutationStatus = evaluateAttributeValue(mutationNode, m_statusAttributeName);

		if (m_skipNoCoverageMutations && STATUS_NO_COVERAGE.equals(mutationStatus)) {
			return false;
		}

		String description = evaluateStringValue(mutationNode, m_descriptionNodeXPath);

		if (description.contains(STACK_DISTANCE_RECORDING_CALL_PACKAGE)) {
			// instrumented code that should not have been there
			return false;
		}

		return true;
	}

	/**
	 * Parse a single mutation node and append the result to the result receiver.
	 * 
	 * @param nodeIndex
	 *            zero-based index of the node
	 */
	protected void parseMutationNodeAndAppendToResultReceiver(Node mutationNode, int nodeIndex,
			IResultReceiver resultReceiver) throws XPathExpressionException {
		MutationSqlOutputBuilder outputBuilder = parseMutationNodeAndCreateOutputBuilder(mutationNode, null);

		if (outputBuilder.isIgnored()) {
			return;
		}

		String killingTestSignatureValue = evaluateStringValue(mutationNode, m_killingTestNodeXPath);
		outputBuilder.setTestSignature(killingTestSignatureValue);
		resultReceiver.append(outputBuilder.toSqlStatement());
	}

	/** Parse a single mutation node (without setting information about the executed test case). */
	protected MutationSqlOutputBuilder parseMutationNodeAndCreateOutputBuilder(Node mutationNode,
			String killingTestSignature) throws XPathExpressionException {
		MutationSqlOutputBuilder mutationSqlOutputBuilder = createOutputBuilder();
		mutationSqlOutputBuilder.setMutationStatus(evaluateAttributeValue(mutationNode, m_statusAttributeName));
		mutationSqlOutputBuilder.setMutatedMethod(evaluateStringValue(mutationNode, m_mutatedClassNodeXPath),
				evaluateStringValue(mutationNode, m_mutatedMethodNodeXPath),
				evaluateStringValue(mutationNode, m_methodTypeSignatureNodeXPath));
		mutationSqlOutputBuilder.setMutatorName(evaluateStringValue(mutationNode, m_mutatorNameNodeXPath));
		mutationSqlOutputBuilder.setTestSignature(killingTestSignature);
		mutationSqlOutputBuilder.setMutationDescription(evaluateStringValue(mutationNode, m_descriptionNodeXPath));
		mutationSqlOutputBuilder.setMutationIndex(evaluateIntValue(mutationNode, m_indexNodeXPath));
		mutationSqlOutputBuilder.setLineNumber(evaluateIntValue(mutationNode, m_lineNumberNodeXPath));
		return mutationSqlOutputBuilder;
	}

	protected MutationSqlOutputBuilder createOutputBuilder() {
		return getResultPresentation().createMutationSqlOutputBuilder("testcase", "testcaseOrig");
	}

	/** @see #m_skipNoCoverageMutations */
	public void setSkipNoCoverageMutations(boolean skipNoCoverageMutations) {
		m_skipNoCoverageMutations = skipNoCoverageMutations;
	}
}
