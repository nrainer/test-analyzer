package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Coverage parser for JaCoCo XML files. */
public class JaCoCoXmlParser implements ICoverageParser {

	private static final String XML_SCHEMA_NAME = "report.dtd";

	private static final String COUNTER_TYPE_METHOD = "METHOD";
	private static final String COUNTER_TYPE_INSTRUCTION = "INSTRUCTION";
	private static final String COUNTER_TYPE_BRANCH = "BRANCH";

	private final List<String> m_result;
	private DocumentBuilder m_documentBuilder;
	private XPath m_xPath;

	/** Constructor. */
	public JaCoCoXmlParser() {
		m_result = new ArrayList<>();
	}

	/** {@inheritDoc} */
	@Override
	public void initialize() throws ParserConfigurationException {
		m_documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		m_documentBuilder.setEntityResolver(new CoverageReportEntityResolver());

		XPathFactory xPathFactory = XPathFactory.newInstance();
		m_xPath = xPathFactory.newXPath();
	}

	/** {@inheritDoc} */
	@Override
	public void parse(File inputFile) throws SAXException, IOException {
		Document document = m_documentBuilder.parse(inputFile);

		m_result.addAll(parseSourceFolderInformation(document));
		m_result.addAll(parseMethodInformation(document));
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getResult() {
		return m_result;
	}

	/** {@inheritDoc} */
	@Override
	public void resetResult() {
		m_result.clear();
	}

	private List<String> parseSourceFolderInformation(Document document) {
		XPathExpression expr = m_xPath.compile("/report/group/group/counter[@type='" + type + "']");
		NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			expr = m_xPath.compile("../@name");
			String groupName = (String) expr.evaluate(node, XPathConstants.STRING);

			expr = m_xPath.compile("./@covered");
			double countCovered = Double.parseDouble((String) expr.evaluate(node, XPathConstants.STRING));

			expr = m_xPath.compile("./@missed");
			double countNotCovered = Double.parseDouble((String) expr.evaluate(node, XPathConstants.STRING));

			m_result.add(String.format(SQL_INSERT_PROJECT_COVERAGE, groupName, countCovered,
					countCovered + countNotCovered));
		}
	}

	private String evaluateStringValue(XPathExpression expression, Node node) throws XPathExpressionException {
		return (String) expression.evaluate(node, XPathConstants.STRING);
	}

	private List<String> parseMethodInformation(Document document) throws XPathExpressionException {
		parseClassNodes(document);
		return null;
	}

	private void parseClassNodes(Document document) throws XPathExpressionException {
		XPathExpression allClassesXPath = m_xPath.compile("//class");

		NodeList classNodes = (NodeList) allClassesXPath.evaluate(document, XPathConstants.NODESET);

		for (int i = 0; i < classNodes.getLength(); i++) {
			parseClassNode(classNodes.item(i));
		}
	}

	private void parseClassNode(Node classNode) throws XPathExpressionException {
		XPathExpression classNameAttributeXPath = m_xPath.compile("@name");
		XPathExpression methodsOfClassXPath = m_xPath.compile("./method");

		String className = JavaUtility
				.toClassName((String) classNameAttributeXPath.evaluate(classNode, XPathConstants.STRING));

		NodeList methodNodes = (NodeList) methodsOfClassXPath.evaluate(classNode, XPathConstants.NODESET);

		for (int i = 0; i < methodNodes.getLength(); i++) {
			parseMethodNode(className, methodNodes.item(i));
		}
	}

	private void parseMethodNode(String className, Node methodNode) throws XPathExpressionException {
		XPathExpression methodNameAttributeXPath = m_xPath.compile("@name");
		XPathExpression methodDescAttributeXPath = m_xPath.compile("@desc");

		String methodName = evaluateStringValue(methodNameAttributeXPath, methodNode);

		if (BytecodeUtility.isConstructor(methodName)) {
			return;
		}

		String methodDesc = evaluateStringValue(methodDescAttributeXPath, methodNode);

		MethodIdentifier methodIdentifier = MethodIdentifier.create(className, methodName, methodDesc);

		parseMethodNodeCoverage(methodIdentifier, methodNode, COUNTER_TYPE_INSTRUCTION);
		parseMethodNodeCoverage(methodIdentifier, methodNode, COUNTER_TYPE_BRANCH);
	}

	private void parseMethodNodeCoverage(MethodIdentifier methodIdentifier, Node methodNode, String coverageType)
			throws XPathExpressionException {
		XPathExpression expr = m_xPath.compile(String.format("./counter[@type='%s']", coverageType));
		double instructionCoverage = getCoverageInPercent((Node) expr.evaluate(methodNode, XPathConstants.NODE));

		m_result.add(
				String.format(SQL_INSERT_METHOD_COVERAGE_INSTRUCTIONS, methodIdentifier.get(), instructionCoverage));
	}

	private static class CoverageReportEntityResolver implements EntityResolver {

		/** {@inheritDoc} */
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (XML_SCHEMA_NAME.endsWith(systemId)) {
				return new InputSource(new StringReader(""));
			}

			return null;
		}
	}

	private double getCoverageValue(Node counterNode) throws XPathExpressionException {
		XPathExpression expr;
		double covered;
		double missed;

		expr = m_xPath.compile("@covered");
		covered = Integer.parseInt((String) expr.evaluate(counterNode, XPathConstants.STRING));

		expr = m_xPath.compile("@missed");
		missed = Integer.parseInt((String) expr.evaluate(counterNode, XPathConstants.STRING));

		return (covered / (covered + missed)) * 100;
	}

}
