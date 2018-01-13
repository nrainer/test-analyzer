package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;

/** Abstract test for {@link IContentParser}. */
public abstract class AbstractContentParserTest {

	/** Input file name. */
	private String m_inputFileName;

	/** Expected result file name. */
	private String m_expectedResultFileName;

	/** Constructor. */
	public AbstractContentParserTest(String inputFileName, String expectedResultFileName) {
		m_inputFileName = inputFileName;
		m_expectedResultFileName = expectedResultFileName;
	}

	/** Test. */
	@Test
	public void testParserWithDefaultConfiguration() throws Exception {
		IContentParser parser = createParser();
		testParser(parser, m_inputFileName, m_expectedResultFileName);
	}

	protected void testParser(IContentParser parser, String inputFileName, String expectedResultFileName)
			throws ContentParserException, IOException {
		parser.initialize();

		File inputFile = new File(TestUtility.getTestFolder(getClass()) + inputFileName);
		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();

		parser.parse(inputFile, resultReceiver);
		List<String> expectedResultLines = TextFileUtility
				.readFromFile(TestUtility.getTestFolder(getClass()) + expectedResultFileName);

		String expectedResult = StringUtility.join(expectedResultLines, CommonConstants.NEW_LINE);
		String actualResult = StringUtility.join(resultReceiver.getResult(), CommonConstants.NEW_LINE);

		expectedResult = expectedResult.replace("\r\n", "\n");
		actualResult = actualResult.replace("\r\n", "\n");

		assertEquals(expectedResult, actualResult);
	}

	/** Create a parser. */
	protected abstract IContentParser createParser();
}
