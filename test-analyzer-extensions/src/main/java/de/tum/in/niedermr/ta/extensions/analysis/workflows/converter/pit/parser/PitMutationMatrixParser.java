package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.parser;

import java.util.List;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.result.MutationSqlOutputBuilder;

/**
 * Coverage parser for modified PIT XML files that contain data to create a mutation matrix. <br/>
 * {@link m_successfulTestNodeXPath} and {@link m_killingTestNodeXPath} can contain multiple test cases separated by
 * {@link m_testcaseUnrollingSeparator}.
 */
public class PitMutationMatrixParser extends PitResultParser {

	private static final String MUTATION_STATUS_SURVIVED = "SURVIVED";

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(PitMutationMatrixParser.class);

	/** Killing test node of mutation node. */
	private XPathExpression m_succeedingTestNodeXPath;

	/** Separator for test cases. */
	private String m_testcaseUnrollingSeparator;

	/** Constructor. */
	public PitMutationMatrixParser(IExecutionId executionId, String testcaseUnrollingSeparator) {
		super(executionId);
		m_testcaseUnrollingSeparator = testcaseUnrollingSeparator;
	}

	/** {@inheritDoc} */
	@Override
	protected void initializeXPathExpressions() throws XPathExpressionException {
		super.initializeXPathExpressions();

		m_succeedingTestNodeXPath = compileXPath("./succeedingTest");
	}

	/** {@inheritDoc} */
	@Override
	protected void parseMutationNodeAndAppendToResultReceiver(int nodeIndex, Node mutationNode,
			IResultReceiver resultReceiver) throws XPathExpressionException {
		MutationSqlOutputBuilder outputBuilder = parseMutationNodeAndCreateOutputBuilder(mutationNode, null);
		final String mutationStatusOfXmlNode = outputBuilder.getMutationStatus();

		String[] successfulTestcaseSignatures = splitTestcases(
				evaluateStringValue(mutationNode, m_succeedingTestNodeXPath));
		String[] killingTestcaseSignatures = splitTestcases(evaluateStringValue(mutationNode, m_killingTestNodeXPath));

		if (MUTATION_STATUS_SURVIVED.equals(mutationStatusOfXmlNode) && successfulTestcaseSignatures.length == 0) {
			// no information about successful test cases has been recorded (note that the status must be taken into
			// account because the status NO_COVERAGE has no attached test cases)
			LOGGER.warn("No information about successful test cases has been recorded for node " + nodeIndex);
			// result with null as test case will be added by the next if block
		}

		if (successfulTestcaseSignatures.length == 0 && killingTestcaseSignatures.length == 0) {
			outputBuilder.setTestSignature(null);
			resultReceiver.append(outputBuilder.complete());
			return;
		}

		// add test cases that were executed with success (and thus, did not kill the mutant)
		for (String testcaseSignature : successfulTestcaseSignatures) {
			outputBuilder.setMutationStatus(MUTATION_STATUS_SURVIVED);
			outputBuilder.setTestSignature(testcaseSignature);
			resultReceiver.append(outputBuilder.complete());
		}

		// add test cases that were not executed with success (and killed the mutant)
		for (String testcaseSignature : killingTestcaseSignatures) {
			outputBuilder.setMutationStatus(mutationStatusOfXmlNode);
			outputBuilder.setTestSignature(testcaseSignature);
			resultReceiver.append(outputBuilder.complete());
		}
	}

	private String[] splitTestcases(String testcaseSignatures) {
		if (StringUtility.isNullOrEmpty(testcaseSignatures)) {
			return new String[0];
		}

		return testcaseSignatures.split(Pattern.quote(m_testcaseUnrollingSeparator));
	}

	/** {@inheritDoc} */
	@Override
	protected List<String> getOutputFileHeader() {
		List<String> headerLines = super.getOutputFileHeader();
		headerLines.add(getResultPresentation().formatLineComment(
				"Mutation nodes were unrolled to an insert statement for each killing and non-killing test case."));
		return headerLines;
	}
}
