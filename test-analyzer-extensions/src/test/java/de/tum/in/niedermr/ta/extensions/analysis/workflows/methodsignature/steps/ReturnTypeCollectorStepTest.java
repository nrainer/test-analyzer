package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.execution.TestExecutionContext;

/** Test {@link ReturnTypeCollectorStep}. */
public class ReturnTypeCollectorStepTest {

	/** Execution step. */
	private ReturnTypeCollectorStep m_executionStep;

	/** Type occurrences. */
	private Map<String, Integer> m_typeOccurrences;

	/** Result receiver. */
	private InMemoryResultReceiver m_resultReceiver;

	/** Before. */
	@Before
	public void before() {
		m_executionStep = AbstractWorkflow.createAndInitializeExecutionStep(TestExecutionContext.create(),
				ReturnTypeCollectorStep.class);

		m_typeOccurrences = new HashMap<>();
		m_typeOccurrences.put(String.class.getName(), 10);
		m_typeOccurrences.put(Double.class.getName(), 5);
		m_typeOccurrences.put(Long.class.getName(), 33);
		m_typeOccurrences.put(Object.class.getName(), 6);
		m_typeOccurrences.put(Serializable.class.getName(), 23);
		m_typeOccurrences.put(Pattern.class.getName(), 8);
		m_typeOccurrences.put(Function.class.getName(), 11);

		m_resultReceiver = new InMemoryResultReceiver();
		m_executionStep.setResultReceiver(m_resultReceiver);
	}

	/** Test. */
	@Test
	public void testFilterOccurrencesAndCreateOutputAsList() throws IOException {
		m_executionStep.setOutputFormat(OutputFormat.LIST);
		m_executionStep.filterOccurrencesAndCreateOutput(m_typeOccurrences);
		assertEquals(m_resultReceiver.getResult(),
				TextFileUtility.readFromFile(TestUtility.getTestFolder(getClass()) + "expected-list.txt"));
	}

	/** Test. */
	@Test
	public void testFilterOccurrencesAndCreateOutputAsCode() throws IOException {
		m_executionStep.setOutputFormat(OutputFormat.CODE);
		m_executionStep.filterOccurrencesAndCreateOutput(m_typeOccurrences);
		assertEquals(m_resultReceiver.getResult(),
				TextFileUtility.readFromFile(TestUtility.getTestFolder(getClass()) + "expected-code.txt"));

	}

	/** Test. */
	@Test
	public void testFilterOccurrencesAndCreateOutputAsCount1() throws IOException {
		m_executionStep.setOutputFormat(OutputFormat.LIST_WITH_COUNT);
		m_executionStep.filterOccurrencesAndCreateOutput(m_typeOccurrences);
		assertEquals(m_resultReceiver.getResult(),
				TextFileUtility.readFromFile(TestUtility.getTestFolder(getClass()) + "expected-count-1.txt"));
	}

	/** Test. */
	@Test
	public void testFilterOccurrencesAndCreateOutputAsCount2() throws IOException {
		m_executionStep.setOutputFormat(OutputFormat.LIST_WITH_COUNT);
		m_executionStep.setExcludeWrapperAndString(true);
		m_executionStep.setMinTypeOccurrenceCount(5);
		m_executionStep.setClassNameFilter(Optional.of(className -> !className.contains("Function")));
		m_executionStep.filterOccurrencesAndCreateOutput(m_typeOccurrences);
		assertEquals(m_resultReceiver.getResult(),
				TextFileUtility.readFromFile(TestUtility.getTestFolder(getClass()) + "expected-count-2.txt"));
	}
}
