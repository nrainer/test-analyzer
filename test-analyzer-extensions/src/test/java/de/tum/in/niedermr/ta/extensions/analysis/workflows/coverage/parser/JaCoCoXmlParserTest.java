package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link JaCoCoXmlParser}. */
public class JaCoCoXmlParserTest {

	@Test
	public void testParser() throws Exception {
		ICoverageParser jaCoCoXmlParser = new JaCoCoXmlParser(ExecutionIdFactory.ID_FOR_TESTS);
		jaCoCoXmlParser.initialize();

		File coverageXmlInputFile = new File(TestUtility.getTestFolder(getClass()) + "coverage.xml");

		jaCoCoXmlParser.parse(coverageXmlInputFile);
		List<String> expectedOutput = TextFileData
				.readFromFile(TestUtility.getTestFolder(getClass()) + "expected.sql.txt");
		assertEquals(expectedOutput, jaCoCoXmlParser.getResult());
	}
}
