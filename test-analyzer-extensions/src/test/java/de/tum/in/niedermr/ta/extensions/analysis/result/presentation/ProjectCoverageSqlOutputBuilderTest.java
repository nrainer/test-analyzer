package de.tum.in.niedermr.ta.extensions.analysis.result.presentation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageLevel;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link ProjectCoverageSqlOutputBuilder}. */
public class ProjectCoverageSqlOutputBuilderTest {

	@Test
	public void testOutputBuilder() throws IOException {
		ProjectCoverageSqlOutputBuilder builder = new ProjectCoverageSqlOutputBuilder(ExecutionIdFactory.ID_FOR_TESTS,
				ECoverageLevel.BRANCH);
		builder.addSourceFolder("src/main/java", 204, 33);
		builder.addSourceFolder("src/test/java", 14, 0);

		List<String> expectedOutput = TextFileUtility
				.readFromFile(TestUtility.getTestFolder(getClass()) + "expected.sql.txt");
		assertEquals(expectedOutput, builder.complete());
	}
}
