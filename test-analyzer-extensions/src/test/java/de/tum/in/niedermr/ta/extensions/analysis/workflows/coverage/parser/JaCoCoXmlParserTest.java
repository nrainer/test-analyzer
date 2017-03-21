package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link JaCoCoXmlParser}. */
public class JaCoCoXmlParserTest {

	/** Test. */
	@Test
	public void testParser() throws Exception {
		ICoverageParser jaCoCoXmlParser = new JaCoCoXmlParser(ExecutionIdFactory.ID_FOR_TESTS);
		jaCoCoXmlParser.initialize();

		File coverageXmlInputFile = new File(TestUtility.getTestFolder(getClass()) + "coverage.xml");
		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();

		jaCoCoXmlParser.parse(coverageXmlInputFile, resultReceiver);
		List<String> expectedOutput = TextFileUtility
				.readFromFile(TestUtility.getTestFolder(getClass()) + "expected.sql.txt");
		assertEquals(expectedOutput, resultReceiver.getResult());
	}
}
