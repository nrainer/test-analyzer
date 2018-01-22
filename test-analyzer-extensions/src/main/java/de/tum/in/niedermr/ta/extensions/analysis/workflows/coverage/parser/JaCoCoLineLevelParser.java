package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import java.util.List;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.sql.SqlMultiInsertStatementBuilder;

/** Coverage parser for JaCoCo XML files. */
public class JaCoCoLineLevelParser extends AbstractJaCoCoParser {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(JaCoCoLineLevelParser.class);

	private static final String SQL_INSERT_INTO_COMMIT_INFO = "INSERT IGNORE INTO Commit_Info "
			+ "(projectKey, commitHash) " + "VALUES (@projectKey, @commitHash);";
	private static final String SQL_INSERT_INTO_METHOD_LOCATION_INFORMATION = "INSERT INTO Method_Location_Import "
			+ "(projectKey, commitHash, className, methodShortName, methodDesc, startLine) "
			+ "VALUES (@projectKey, @commitHash, '%s', '%s', '%s', %s);";
	private static final String SQL_INSERT_INTO_LINE_COVERAGE_SHALLOW = "INSERT INTO Line_Coverage_Import "
			+ "(projectKey, commitHash, sessionNumber, packageName, sourceFileName, lineNumber, coverageState) VALUES %s;";
	private static final String SQL_INSERT_INTO_LINE_COVERAGE_VALUES = "(@projectKey, @commitHash, @sessionNumber, '%s', '%s', %s, '%s')";

	private XPathExpression m_classNameAttributeXPath;
	private XPathExpression m_methodNameAttributeXPath;
	private XPathExpression m_methodDescAttributeXPath;
	private XPathExpression m_methodLineAttributeXPath;

	private XPathExpression m_allPackagesXPath;
	private XPathExpression m_packageNameAttributeXPath;
	private XPathExpression m_sourceFilesOfPackageXPath;
	private XPathExpression m_sourceFileNameAttributeXPath;
	private XPathExpression m_sourceLinesOfFileXPath;
	private XPathExpression m_numberOfSourceLineAttributeXPath;
	private XPathExpression m_coveredInstructionsAttributeXPath;
	private XPathExpression m_missedInstructionsAttributeXPath;

	/** Constructor. */
	public JaCoCoLineLevelParser(IExecutionId executionId) {
		super(executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void execCompileXPathExpressions() throws XPathExpressionException {
		super.execCompileXPathExpressions();

		m_classNameAttributeXPath = compileXPath("@name");
		m_methodNameAttributeXPath = compileXPath("@name");
		m_methodDescAttributeXPath = compileXPath("@desc");
		m_methodLineAttributeXPath = compileXPath("@line");
		m_allPackagesXPath = compileXPath("//package");
		m_packageNameAttributeXPath = compileXPath("@name");
		m_sourceFilesOfPackageXPath = compileXPath("./sourcefile");
		m_sourceFileNameAttributeXPath = compileXPath("@name");
		m_sourceLinesOfFileXPath = compileXPath("./line");
		m_numberOfSourceLineAttributeXPath = compileXPath("@nr");
		m_coveredInstructionsAttributeXPath = compileXPath("@ci");
		m_missedInstructionsAttributeXPath = compileXPath("@mi");
	}

	/** {@inheritDoc} */
	@Override
	protected void parse(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		LOGGER.info("Begin parsing method location information");
		parseMethodLocationInformation(document, resultReceiver);
		LOGGER.info("Completed parsing method location information");

		resultReceiver.markResultAsPartiallyComplete();

		LOGGER.info("Begin parsing line level information");
		parseLineCoverage(document, resultReceiver);
		LOGGER.info("Completed parsing line level information");

		resultReceiver.markResultAsComplete();
	}

	/** {@inheritDoc} */
	@Override
	protected void execAppendToOutputFileHeader(List<String> header) {
		super.execAppendToOutputFileHeader(header);

		header.add("");
		header.add(getResultPresentation().formatLineComment("BEGIN VARIABLES"));
		header.add("SET @projectKey = '###';");
		header.add("SET @commitHash = '###';");
		header.add("SET @sessionNumber = '###';");
		header.add(getResultPresentation().formatLineComment("END VARIABLES"));
		header.add("");

		header.add(SQL_INSERT_INTO_COMMIT_INFO);
	}

	private void parseMethodLocationInformation(Document document, IResultReceiver resultReceiver)
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
		String className = evaluateStringValue(classNode, m_classNameAttributeXPath);

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
		String methodLine = evaluateStringValue(methodNode, m_methodLineAttributeXPath);

		resultReceiver.append(String.format(SQL_INSERT_INTO_METHOD_LOCATION_INFORMATION, className, methodName,
				methodDesc, methodLine));
	}

	private void parseLineCoverage(Document document, IResultReceiver resultReceiver) throws XPathExpressionException {
		NodeList packages = evaluateNodeList(document, m_allPackagesXPath);

		visitNodes(packages, resultReceiver, new INodeVisitor() {
			/** {@inheritDoc} */
			@Override
			public void visitNode(Node packageNode, int nodeIndex, IResultReceiver resultReceiver) throws XPathExpressionException {
				parsePackageNode(packageNode, resultReceiver);
			}
		});
	}

	private void parsePackageNode(Node packageNode, IResultReceiver resultReceiver) throws XPathExpressionException {
		String packageName = evaluateStringValue(packageNode, m_packageNameAttributeXPath);
		NodeList sourceFiles = evaluateNodeList(packageNode, m_sourceFilesOfPackageXPath);

		visitNodes(sourceFiles, resultReceiver, new INodeVisitor() {
			/** {@inheritDoc} */
			@Override
			public void visitNode(Node sourceFileNode, int nodeIndex, IResultReceiver resultReceiver) throws XPathExpressionException {
				parseSourceFile(packageName, sourceFileNode, resultReceiver);
			}
		});

		LOGGER.info("Parsed package: " + packageName);
	}

	private void parseSourceFile(String packageName, Node sourceFileNode, IResultReceiver resultReceiver)
			throws XPathExpressionException {
		String sourceFileName = evaluateStringValue(sourceFileNode, m_sourceFileNameAttributeXPath);
		String sourceFileNameWithoutEnding = sourceFileName.replaceAll("\\.java$", "");
		NodeList sourceLines = evaluateNodeList(sourceFileNode, m_sourceLinesOfFileXPath);

		SqlMultiInsertStatementBuilder sqlStatementBuilder = new SqlMultiInsertStatementBuilder(
				SQL_INSERT_INTO_LINE_COVERAGE_SHALLOW);

		visitNodes(sourceLines, resultReceiver, new INodeVisitor() {
			/** {@inheritDoc} */
			@Override
			public void visitNode(Node sourceLineNode, int nodeIndex, IResultReceiver resultReceiver) throws XPathExpressionException {
				parseSourceLineNode(packageName, sourceFileNameWithoutEnding, sourceLineNode, resultReceiver,
						sqlStatementBuilder);
			}
		});

		resultReceiver.append(sqlStatementBuilder.toSql());
	}

	private void parseSourceLineNode(String packageName, String sourceFileName, Node sourceLineNode,
			IResultReceiver resultReceiver, SqlMultiInsertStatementBuilder sqlStatementBuilder)
			throws XPathExpressionException {
		String lineNumber = evaluateStringValue(sourceLineNode, m_numberOfSourceLineAttributeXPath);
		int countCoveredInstructions = evaluateIntValue(sourceLineNode, m_coveredInstructionsAttributeXPath);
		int countMissedInstructions = evaluateIntValue(sourceLineNode, m_missedInstructionsAttributeXPath);

		ELineCoverageState lineCoverageState = ELineCoverageState.get(countCoveredInstructions,
				countMissedInstructions);

		String sqlValuesStatementPart = String.format(SQL_INSERT_INTO_LINE_COVERAGE_VALUES, packageName, sourceFileName,
				lineNumber, lineCoverageState.getName());
		sqlStatementBuilder.addValuesStatementPart(sqlValuesStatementPart);
	}
}
