package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;

/** Abstract XML content parser. */
public abstract class AbstractXmlContentParser implements IContentParser {

	private final String m_xmlSchemaName;
	private final IResultPresentationExtended m_resultPresentation;
	private DocumentBuilder m_documentBuilder;
	private XPath m_xPath;

	/** Constructor. */
	public AbstractXmlContentParser(String xmlSchemaName, IExecutionId executionId) {
		m_xmlSchemaName = xmlSchemaName;
		m_resultPresentation = IResultPresentationExtended.create(executionId);
	}

	/** {@inheritDoc} */
	@Override
	public void initialize() throws ContentParserException {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			configureDocumentBuilderFactory(documentBuilderFactory);
			m_documentBuilder = documentBuilderFactory.newDocumentBuilder();
			m_documentBuilder.setEntityResolver(new NoSchemaReportEntityResolver());

			XPathFactory xPathFactory = XPathFactory.newInstance();
			m_xPath = xPathFactory.newXPath();
		} catch (ParserConfigurationException e) {
			throw new ContentParserException("Parser initialization failed", e);
		}

		try {
			execCompileXPathExpressions();
		} catch (XPathExpressionException e) {
			throw new ContentParserException("XPath expression compilation failed", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void parse(File inputFile, IResultReceiver resultReceiver) throws ContentParserException {
		try {
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
			Document document = m_documentBuilder.parse(inputStream);
			resultReceiver.append(getOutputFileHeader());
			parse(document, resultReceiver);
			resultReceiver.markResultAsComplete();
		} catch (SAXException | IOException | XPathExpressionException e) {
			throw new ContentParserException("Parser exception", e);
		}
	}

	/** Parse. */
	protected abstract void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException;

	/** Configure the document builder factory. */
	protected void configureDocumentBuilderFactory(DocumentBuilderFactory documentBuilderFactory)
			throws ParserConfigurationException {
		documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	}

	/** Compile an XPath expression. */
	protected final XPathExpression compileXPath(String expression) throws XPathExpressionException {
		return m_xPath.compile(expression);
	}

	/** Compile expressions. */
	protected void execCompileXPathExpressions() throws XPathExpressionException {
		// NOP
	}

	/** Get the extended result presentation. */
	protected final IResultPresentationExtended getResultPresentation() {
		return m_resultPresentation;
	}

	/** Evaluate an expression on a node to a String value. */
	protected static String evaluateStringValue(Node node, XPathExpression expression) throws XPathExpressionException {
		return (String) expression.evaluate(node, XPathConstants.STRING);
	}

	/** Evaluate an expression on a node to a node list. */
	protected static NodeList evaluateNodeList(Node node, XPathExpression expression) throws XPathExpressionException {
		return (NodeList) expression.evaluate(node, XPathConstants.NODESET);
	}

	/** Evaluate the value of a node attribute. */
	protected static String evaluateAttributeValue(Node node, String attributeName) {
		Node attributeNode = node.getAttributes().getNamedItem(attributeName);

		if (attributeNode == null) {
			return null;
		}

		return attributeNode.getNodeValue();
	}

	/** Entity resolver which does not require a schema. */
	private class NoSchemaReportEntityResolver implements EntityResolver {

		/** {@inheritDoc} */
		@Override
		public InputSource resolveEntity(String publicId, String systemId) {
			if (systemId.endsWith(m_xmlSchemaName)) {
				// do not require the schema
				return new InputSource(new StringReader(""));
			}

			return null;
		}
	}

	protected List<String> getOutputFileHeader() {
		List<String> outputHeader = new ArrayList<>();
		outputHeader.add(getResultPresentation().formatLineComment("Created with: " + getClass().getName()));
		return outputHeader;
	}
}
