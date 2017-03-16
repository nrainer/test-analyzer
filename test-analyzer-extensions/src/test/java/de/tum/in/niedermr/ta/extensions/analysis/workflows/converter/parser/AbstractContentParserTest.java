package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;

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
	public void testParser() throws Exception {
		IContentParser parser = createParser();
		parser.initialize();

		File inputFileName = new File(TestUtility.getTestFolder(getClass()) + m_inputFileName);
		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();

		parser.parse(inputFileName, resultReceiver);
		List<String> expectedOutput = TextFileUtility
				.readFromFile(TestUtility.getTestFolder(getClass()) + m_expectedResultFileName);
		assertEquals(expectedOutput, resultReceiver.getResult());
	}

	/** Create a parser. */
	protected abstract IContentParser createParser();
}
