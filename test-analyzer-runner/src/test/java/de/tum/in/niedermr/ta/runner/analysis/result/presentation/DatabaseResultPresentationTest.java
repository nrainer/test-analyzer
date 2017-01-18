package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link DatabaseResultPresentation}. */
public class DatabaseResultPresentationTest {

	/** Test. */
	@Test
	public void testTryParseExecutionIdFromExecutionInformation() throws ReflectiveOperationException {
		assertEquals(Optional.empty(),
				DatabaseResultPresentation.tryParseExecutionIdFromExecutionInformation(Arrays.asList()));
		assertEquals(Optional.empty(),
				DatabaseResultPresentation.tryParseExecutionIdFromExecutionInformation(Arrays.asList("abc", "cde")));

		IExecutionId executionId = ExecutionIdFactory.createNewShortExecutionId();
		IResultPresentation resultPresentation = ResultPresentationUtil
				.createResultPresentation(DatabaseResultPresentation.class.getName(), executionId);

		String executionInformation = resultPresentation
				.formatExecutionInformation(Arrays.asList("configurationKey1=configurationValue1"));

		Optional<IExecutionId> parsedExecutionId = DatabaseResultPresentation
				.tryParseExecutionIdFromExecutionInformation(Arrays.asList(executionInformation, "# further line"));
		assertTrue(parsedExecutionId.isPresent());
		assertEquals(executionId, parsedExecutionId.get());
	}
}
