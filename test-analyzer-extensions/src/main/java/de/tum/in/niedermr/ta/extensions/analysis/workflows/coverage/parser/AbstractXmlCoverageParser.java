package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;

/** Abstract XML coverage parser. */
public abstract class AbstractXmlCoverageParser implements ICoverageParser {

	private final String m_xmlSchemaName;
	private final IResultPresentationExtended m_resultPresentation;
	private DocumentBuilder m_documentBuilder;
	private XPath m_xPath;

	/** Constructor. */
	public AbstractXmlCoverageParser(String xmlSchemaName, IExecutionId executionId) {
		m_xmlSchemaName = xmlSchemaName;
		m_resultPresentation = IResultPresentationExtended.create(executionId);
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
	public void parse(File inputFile, IResultReceiver resultReceiver) throws Exception {
		Document document = m_documentBuilder.parse(inputFile);
		parse(document, resultReceiver);
	}

	protected abstract void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException;

	protected XPathExpression compileXPath(String expression) throws XPathExpressionException {
		return m_xPath.compile(expression);
	}

	protected IResultPresentationExtended getResultPresentation() {
		return m_resultPresentation;
	}

	private class CoverageReportEntityResolver implements EntityResolver {

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
