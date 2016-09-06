package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import javax.xml.xpath.XPathConstants;
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
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.ProjectCoverageSqlOutputBuilder;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageLevel;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageValueType;

/** Coverage parser for JaCoCo XML files. */
public class JaCoCoXmlParser extends AbstractXmlCoverageParser {

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(JaCoCoXmlParser.class);

	private static final String XML_SCHEMA_NAME = "report.dtd";

	private static final String COUNTER_TYPE_METHOD = "METHOD";
	private static final String COUNTER_TYPE_LINE = "LINE";
	private static final String COUNTER_TYPE_INSTRUCTION = "INSTRUCTION";
	private static final String COUNTER_TYPE_BRANCH = "BRANCH";

	public JaCoCoXmlParser(IExecutionId executionId) {
		super(XML_SCHEMA_NAME, executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		LOG.info("Begin parsing source folder information");
		parseSourceFolderInformation(document, resultReceiver);
		LOG.info("Completed parsing source folder information");

		LOG.info("Begin parsing method information");
		parseMethodInformation(document, resultReceiver);
		LOG.info("Completed parsing method information");

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
		ProjectCoverageSqlOutputBuilder sqlOutputBuilder = getResultPresentation()
				.createProjectCoverageSqlOutputBuilder(convertCounterTypeToCoverageLevel(counterTypeName));

		NodeList nodeList = evaluateNodeList(document, sourceFoldersExpression);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			String sourceFolderName = evaluateStringValue(node, folderNameAttributeXPath);
			int countCovered = Integer.parseInt(evaluateStringValue(node, folderCountCoveredAttributeXPath));
			int countNotCovered = Integer.parseInt(evaluateStringValue(node, folderCountMissedAttributeXPath));
			sqlOutputBuilder.addSourceFolder(sourceFolderName, countCovered, countNotCovered);
		}

		resultReceiver.append(sqlOutputBuilder.complete());
	}

	private void parseMethodInformation(Document document, IResultReceiver resultReceiver)
			throws XPathExpressionException {
		XPathExpression allClassesXPath = compileXPath("//class");
		NodeList classNodes = evaluateNodeList(document, allClassesXPath);

		for (int i = 0; i < classNodes.getLength(); i++) {
			parseClassNode(classNodes.item(i), resultReceiver);
		}
	}

	private void parseClassNode(Node classNode, IResultReceiver resultReceiver) throws XPathExpressionException {
		XPathExpression classNameAttributeXPath = compileXPath("@name");
		XPathExpression methodsOfClassXPath = compileXPath("./method");

		String className = JavaUtility.toClassName(evaluateStringValue(classNode, classNameAttributeXPath));
		NodeList methodNodes = evaluateNodeList(classNode, methodsOfClassXPath);
		for (int i = 0; i < methodNodes.getLength(); i++) {
			parseMethodNode(className, methodNodes.item(i), resultReceiver);
		}

		LOG.info("Parsed coverage of methods of class: " + className);
	}

	private void parseMethodNode(String className, Node methodNode, IResultReceiver resultReceiver)
			throws XPathExpressionException {
		XPathExpression methodNameAttributeXPath = compileXPath("@name");
		XPathExpression methodDescAttributeXPath = compileXPath("@desc");

		String methodName = evaluateStringValue(methodNode, methodNameAttributeXPath);
		String methodDesc = evaluateStringValue(methodNode, methodDescAttributeXPath);

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
		XPathExpression folderCountCoveredAttributeXPath = compileXPath("@covered");
		XPathExpression folderCountMissedAttributeXPath = compileXPath("@missed");

		ECoverageLevel coverageLevel = convertCounterTypeToCoverageLevel(counterTypeName);

		Node counterNode = (Node) counterNodeXPath.evaluate(methodNode, XPathConstants.NODE);
		int countCovered = 0;
		int countNotCovered = 0;

		if (counterNode != null) {
			// counterNode is null if a method does not contain any branches
			countCovered = Integer.parseInt(evaluateStringValue(counterNode, folderCountCoveredAttributeXPath));
			countNotCovered = Integer.parseInt(evaluateStringValue(counterNode, folderCountMissedAttributeXPath));
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

	private String evaluateStringValue(Node node, XPathExpression expression) throws XPathExpressionException {
		return (String) expression.evaluate(node, XPathConstants.STRING);
	}

	private NodeList evaluateNodeList(Node node, XPathExpression expression) throws XPathExpressionException {
		return (NodeList) expression.evaluate(node, XPathConstants.NODESET);
	}
}
