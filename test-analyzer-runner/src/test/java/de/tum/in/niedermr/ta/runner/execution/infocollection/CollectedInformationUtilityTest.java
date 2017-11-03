package de.tum.in.niedermr.ta.runner.execution.infocollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.DatabaseResultPresentation;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.ResultPresentationUtil;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Test {@link CollectedInformationUtility}. */
public class CollectedInformationUtilityTest {

	/** Test. */
	@Test
	public void testConvertAndParse() {
		final List<TestInformation> data = getLongTestData();

		Set<TestInformation> expectedResult = new HashSet<>(data);

		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();
		CollectedInformationUtility.convertToParseableMethodTestcaseText(data, resultReceiver);
		Set<TestInformation> parsedData = new HashSet<>(
				CollectedInformationUtility.parseMethodTestcaseText(resultReceiver.getResult()));

		assertEquals(expectedResult, parsedData);
	}

	/** Test. */
	@Test
	public void testConvertToParseableMethodTestcaseText() {
		List<String> expected = new LinkedList<>();
		expected.add("de.tum.in.ma.project.example.SimpleCalculation.getResultAsString()");
		expected.add("de.tum.in.ma.project.example.UnitTest;stringCorrect");
		expected.add(".");

		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();
		CollectedInformationUtility.convertToParseableMethodTestcaseText(getShortTestData(), resultReceiver);

		assertEquals(expected, resultReceiver.getResult());
	}

	/** Test. */
	@Test
	public void testToSQLStatements() throws ReflectiveOperationException {
		final IExecutionId executionId = ExecutionIdFactory.ID_FOR_TESTS;

		IResultPresentation resultPresentation = ResultPresentationUtil.createResultPresentation(
				DatabaseResultPresentation.class.getName(), executionId, getClass().getClassLoader());

		String expected = resultPresentation.formatMethodAndTestcaseMapping(
				MethodIdentifier.parse("de.tum.in.ma.project.example.SimpleCalculation.getResultAsString()"),
				TestcaseIdentifier.create("de.tum.in.ma.project.example.UnitTest", "stringCorrect"));

		InMemoryResultReceiver resultReceiver = new InMemoryResultReceiver();
		CollectedInformationUtility.convertToMethodTestcaseMappingResult(getShortTestData(), resultPresentation,
				resultReceiver);
		List<String> result = resultReceiver.getResult();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(expected, result.get(0));
	}

	private List<TestInformation> getShortTestData() {
		List<TestInformation> data = new LinkedList<>();

		TestInformation testInformation = new TestInformation(
				MethodIdentifier.parse("de.tum.in.ma.project.example.SimpleCalculation.getResultAsString()"));
		testInformation
				.addTestcase(TestcaseIdentifier.create("de.tum.in.ma.project.example.UnitTest", "stringCorrect"));
		data.add(testInformation);

		return data;
	}

	private List<TestInformation> getLongTestData() {
		List<TestInformation> data = new LinkedList<>();

		data.addAll(getShortTestData());

		TestInformation testInformation;

		testInformation = new TestInformation(
				MethodIdentifier.parse("de.tum.in.ma.project.example.SimpleCalculation.increment()"));
		testInformation.addTestcase(TestcaseIdentifier.create("de.tum.in.ma.project.example.UnitTest", "even"));
		testInformation.addTestcase(TestcaseIdentifier.create("de.tum.in.ma.project.example.UnitTest", "increment"));
		data.add(testInformation);

		testInformation = new TestInformation(
				MethodIdentifier.parse("de.tum.in.ma.project.example.SimpleCalculation.isEven()"));
		testInformation.addTestcase(TestcaseIdentifier.create("de.tum.in.ma.project.example.UnitTest", "even"));
		data.add(testInformation);

		return data;
	}
}
