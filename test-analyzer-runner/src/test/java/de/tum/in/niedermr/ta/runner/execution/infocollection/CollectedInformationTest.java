package de.tum.in.niedermr.ta.runner.execution.infocollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.DatabaseResultPresentation;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

public class CollectedInformationTest implements CommonConstants {
	@Test
	public void testParseAndToPlain() {
		final List<TestInformation> data = getLongTestData();

		Set<TestInformation> expectedResult = new HashSet<>(data);

		List<String> plainData = CollectedInformation.toPlainText(data);
		Set<TestInformation> parsedData = new HashSet<>(CollectedInformation.parseInformationCollectorData(plainData));

		assertEquals(expectedResult, parsedData);
	}

	@Test
	public void testToPlainText() {
		List<String> expected = new LinkedList<>();
		expected.add("de.tum.in.ma.project.example.SimpleCalculation.getResultAsString()");
		expected.add("de.tum.in.ma.project.example.UnitTest;stringCorrect");
		expected.add(".");

		List<String> result = CollectedInformation.toPlainText(getShortTestData());

		assertEquals(expected, result);
	}

	@Test
	public void testToSQLStatements() {
		final IExecutionId executionId = ExecutionIdFactory.parseShortExecutionId("TEST");

		String expected = String.format(DatabaseResultPresentation.SQL_INSERT_METHOD_TEST_CASE_MAPPING,
				executionId.getShortId(), "de.tum.in.ma.project.example.SimpleCalculation.getResultAsString()",
				"de.tum.in.ma.project.example.UnitTest.stringCorrect()");

		IResultPresentation resultPresentation = new DatabaseResultPresentation();
		resultPresentation.setExecutionId(executionId);

		List<String> result = CollectedInformation.toResult(getShortTestData(), resultPresentation);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(expected, result.get(0));
	}

	private List<TestInformation> getShortTestData() {
		List<TestInformation> data = new LinkedList<>();

		TestInformation testInformation = new TestInformation(
				MethodIdentifier.parse("de.tum.in.ma.project.example.SimpleCalculation.getResultAsString()"));
		testInformation.addTestcase(TestcaseIdentifier
				.parse("de.tum.in.ma.project.example.UnitTest" + SEPARATOR_DEFAULT + "stringCorrect"));
		data.add(testInformation);

		return data;
	}

	private List<TestInformation> getLongTestData() {
		List<TestInformation> data = new LinkedList<>();

		data.addAll(getShortTestData());

		TestInformation testInformation;

		testInformation = new TestInformation(
				MethodIdentifier.parse("de.tum.in.ma.project.example.SimpleCalculation.increment()"));
		testInformation.addTestcase(
				TestcaseIdentifier.parse("de.tum.in.ma.project.example.UnitTest" + SEPARATOR_DEFAULT + "even"));
		testInformation.addTestcase(
				TestcaseIdentifier.parse("de.tum.in.ma.project.example.UnitTest" + SEPARATOR_DEFAULT + "increment"));
		data.add(testInformation);

		testInformation = new TestInformation(
				MethodIdentifier.parse("de.tum.in.ma.project.example.SimpleCalculation.isEven()"));
		testInformation.addTestcase(
				TestcaseIdentifier.parse("de.tum.in.ma.project.example.UnitTest" + SEPARATOR_DEFAULT + "even"));
		data.add(testInformation);

		return data;
	}
}
