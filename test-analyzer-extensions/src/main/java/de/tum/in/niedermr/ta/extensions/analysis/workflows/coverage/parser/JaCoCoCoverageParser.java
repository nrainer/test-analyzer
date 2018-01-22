package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageLevel;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageValueType;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.result.AggregatedCoverageSqlOutputBuilder;

/** Coverage parser for JaCoCo XML files. */
public class JaCoCoCoverageParser extends AbstractJaCoCoParser {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(JaCoCoCoverageParser.class);

	private XPathExpression m_classNameAttributeXPath;
	private XPathExpression m_methodNameAttributeXPath;
	private XPathExpression m_methodDescAttributeXPath;
	private XPathExpression m_methodCountCoveredAttributeXPath;
	private XPathExpression m_methodCountMissedAttributeXPath;

	/** Constructor. */
	public JaCoCoCoverageParser(IExecutionId executionId) {
		super(executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void execCompileXPathExpressions() throws XPathExpressionException {
		super.execCompileXPathExpressions();

		m_classNameAttributeXPath = compileXPath("@name");
		m_methodNameAttributeXPath = compileXPath("@name");
		m_methodDescAttributeXPath = compileXPath("@desc");
		m_methodCountCoveredAttributeXPath = compileXPath("@covered");
		m_methodCountMissedAttributeXPath = compileXPath("@missed");
	}

	/** {@inheritDoc} */
	@Override
	protected void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		LOGGER.info("Begin parsing source folder information");
		parseSourceFolderInformation(document, resultReceiver);
		LOGGER.info("Completed parsing source folder information");

		resultReceiver.markResultAsPartiallyComplete();

		LOGGER.info("Begin parsing method information");
		parseMethodInformation(document, resultReceiver);
		LOGGER.info("Completed parsing method information");

		resultReceiver.markResultAsComplete();
	}

	private void parseSourceFolderInformation(Document document, IResultReceiver resultReceiver)
			throws XPathExpressionException {
		resultReceiver.append(getResultPresentation().getBlockCommentStart());
		resultReceiver.append(getResultPresentation().formatLineComment("TODO: check test excludes"));
		parseSourceFolderInformation(document, resultReceiver, COUNTER_TYPE_METHOD);
		parseSourceFolderInformation(document, resultReceiver, COUNTER_TYPE_LINE);
		parseSourceFolderInformation(document, resultReceiver, COUNTER_TYPE_INSTRUCTION);
		parseSourceFolderInformation(document, resultReceiver, COUNTER_TYPE_BRANCH);
		resultReceiver.append(getResultPresentation().getBlockCommentEnd());
	}

	private void parseSourceFolderInformation(Document document, IResultReceiver resultReceiver, String counterTypeName)
			throws XPathExpressionException {
		XPathExpression sourceFoldersExpression = compileXPath(
				String.format("/report/group/group/counter[@type='%s']", counterTypeName));
		XPathExpression folderNameAttributeXPath = compileXPath("../@name");
		XPathExpression folderCountCoveredAttributeXPath = compileXPath("@covered");
		XPathExpression folderCountMissedAttributeXPath = compileXPath("@missed");
		AggregatedCoverageSqlOutputBuilder sqlOutputBuilder = getResultPresentation()
				.createProjectCoverageSqlOutputBuilder(convertCounterTypeToCoverageLevel(counterTypeName));

		NodeList nodeList = evaluateNodeList(document, sourceFoldersExpression);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			String sourceFolderName = evaluateStringValue(node, folderNameAttributeXPath);
			int countCovered = Integer.parseInt(evaluateStringValue(node, folderCountCoveredAttributeXPath));
			int countNotCovered = Integer.parseInt(evaluateStringValue(node, folderCountMissedAttributeXPath));
			sqlOutputBuilder.addSourceFolder(sourceFolderName, countCovered, countNotCovered);

			// performance tuning (does not influence indices in the NodeList) must be
			// applied at the end here (because
			// folderNameAttributeXPath accesses a parent attribute)
			node.getParentNode().removeChild(node);
		}

		resultReceiver.append(sqlOutputBuilder.complete());
	}

	private void parseMethodInformation(Document document, IResultReceiver resultReceiver)
			throws XPathExpressionException {
		visitClassNodes(document, resultReceiver, new INodeVisitor() {
			/** {@inheritDoc} */
			@Override
			public void visitNode(Node classNode, int nodeIndex, IResultReceiver resultReceiver) throws XPathExpressionException {
				parseClassNode(classNode, resultReceiver);
			}
		});
	}

	private void parseClassNode(Node classNode, IResultReceiver resultReceiver) throws XPathExpressionException {
		String className = JavaUtility.toClassName(evaluateStringValue(classNode, m_classNameAttributeXPath));

		visitMethodNodes(classNode, resultReceiver, new INodeVisitor() {
			/** {@inheritDoc} */
			@Override
			public void visitNode(Node methodNode, int nodeIndex, IResultReceiver resultReceiver) throws XPathExpressionException {
				parseMethodNode(className, methodNode, resultReceiver);
			}
		});

		LOGGER.info("Parsed coverage of methods of class: " + className);
		resultReceiver.markResultAsPartiallyComplete();
	}

	private void parseMethodNode(String className, Node methodNode, IResultReceiver resultReceiver)
			throws XPathExpressionException {
		String methodName = evaluateStringValue(methodNode, m_methodNameAttributeXPath);
		String methodDesc = evaluateStringValue(methodNode, m_methodDescAttributeXPath);

		if (BytecodeUtility.isConstructor(methodName)) {
			return;
		}

		MethodIdentifier methodIdentifier = MethodIdentifier.create(className, methodName, methodDesc);
		parseMethodNode(methodIdentifier, methodNode, resultReceiver, COUNTER_TYPE_LINE);
		parseMethodNode(methodIdentifier, methodNode, resultReceiver, COUNTER_TYPE_INSTRUCTION);
		parseMethodNode(methodIdentifier, methodNode, resultReceiver, COUNTER_TYPE_BRANCH);
	}

	private void parseMethodNode(MethodIdentifier methodIdentifier, Node methodNode, IResultReceiver resultReceiver,
			String counterTypeName) throws XPathExpressionException {

		XPathExpression counterNodeXPath = compileXPath(String.format("./counter[@type='%s']", counterTypeName));
		ECoverageLevel coverageLevel = convertCounterTypeToCoverageLevel(counterTypeName);

		Node counterNode = evaluateNode(methodNode, counterNodeXPath);
		int countCovered = 0;
		int countNotCovered = 0;

		if (counterNode != null) {
			// counterNode is null if a method does not contain any branches
			countCovered = Integer.parseInt(evaluateStringValue(counterNode, m_methodCountCoveredAttributeXPath));
			countNotCovered = Integer.parseInt(evaluateStringValue(counterNode, m_methodCountMissedAttributeXPath));
		}

		resultReceiver.append(getResultPresentation().formatCoveragePerMethod(methodIdentifier, coverageLevel,
				countCovered, ECoverageValueType.COVERED_COUNT));
		resultReceiver.append(getResultPresentation().formatCoveragePerMethod(methodIdentifier, coverageLevel,
				countCovered + countNotCovered, ECoverageValueType.ALL_COUNT));
	}

	private ECoverageLevel convertCounterTypeToCoverageLevel(String counterType) {
		switch (counterType) {
		case COUNTER_TYPE_METHOD:
			return ECoverageLevel.METHOD;
		case COUNTER_TYPE_LINE:
			return ECoverageLevel.LINE;
		case COUNTER_TYPE_INSTRUCTION:
			return ECoverageLevel.INSTRUCTION;
		case COUNTER_TYPE_BRANCH:
			return ECoverageLevel.BRANCH;
		default:
			throw new IllegalArgumentException("Unknown counter type: " + counterType);
		}
	}
}
