package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

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
			m_documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			m_documentBuilder.setEntityResolver(new NoSchemaReportEntityResolver());

			XPathFactory xPathFactory = XPathFactory.newInstance();
			m_xPath = xPathFactory.newXPath();
		} catch (ParserConfigurationException e) {
			throw new ContentParserException("Parser initialization failed", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void parse(File inputFile, IResultReceiver resultReceiver) throws ContentParserException {
		try {
			Document document = m_documentBuilder.parse(inputFile);
			parse(document, resultReceiver);
			resultReceiver.markResultAsComplete();
		} catch (SAXException | IOException | XPathExpressionException e) {
			throw new ContentParserException("Parser exception", e);
		}
	}

	/** Parse. */
	protected abstract void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException;

	/** Compile an XPath expression. */
	protected final XPathExpression compileXPath(String expression) throws XPathExpressionException {
		return m_xPath.compile(expression);
	}

	/** Get the extended result presentation. */
	protected final IResultPresentationExtended getResultPresentation() {
		return m_resultPresentation;
	}

	/** Evaluate an expression on a node to a String value. */
	protected final String evaluateStringValue(Node node, XPathExpression expression) throws XPathExpressionException {
		return (String) expression.evaluate(node, XPathConstants.STRING);
	}

	/** Evaluate an expression on a node to a node list. */
	protected final NodeList evaluateNodeList(Node node, XPathExpression expression) throws XPathExpressionException {
		return (NodeList) expression.evaluate(node, XPathConstants.NODESET);
	}

	/** Entity resolver which does not require a schema. */
	private class NoSchemaReportEntityResolver implements EntityResolver {

		/** {@inheritDoc} */
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (systemId.endsWith(m_xmlSchemaName)) {
				// do not require the schema
				return new InputSource(new StringReader(""));
			}

			return null;
		}
	}
}
