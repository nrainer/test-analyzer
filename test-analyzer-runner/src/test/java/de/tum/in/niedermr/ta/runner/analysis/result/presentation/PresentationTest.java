package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.junit.JUnitTestRunResult;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.sample.junit.SampleJUnitTestClass;
import junit.framework.TestCase;

public class PresentationTest implements CommonConstants {
	private static final TestcaseIdentifier SAMPLE_TEST_IDENTIFIER = TestcaseIdentifier.create(TestCase.class, "test");
	private static final MethodIdentifier SAMPLE_METHOD_IDENTIFIER = MethodIdentifier.create(Object.class, "hashCode",
			"()I");
	private static final String SAMPLE_RET_VAL_GEN_NAME = SimpleReturnValueGeneratorWith0.class.getName();

	@Test
	public void testGetResultPresentation() throws ReflectiveOperationException {
		assertEquals(DatabaseResultPresentation.class,
				ResultPresentationUtil.createResultPresentationWithoutExecutionId(
						ResultPresentationProperty.RESULT_PRESENTATION_DB, getClass().getClassLoader()).getClass());
		assertEquals(TextResultPresentation.class, ResultPresentationUtil.createResultPresentationWithoutExecutionId(
				ResultPresentationProperty.RESULT_PRESENTATION_TEXT, getClass().getClassLoader()).getClass());
		assertEquals(TextResultPresentation.class, ResultPresentationUtil.createResultPresentationWithoutExecutionId(
				TextResultPresentation.class.getName(), getClass().getClassLoader()).getClass());
	}

	@Test
	public void testTextResultPresentation() throws Exception {
		IResultPresentation presentation = new TextResultPresentation();

		final String genericExpected = "" + "Testcase: junit.framework.TestCase.test()" + NEW_LINE
				+ "Mutated method: java.lang.Object.hashCode()" + NEW_LINE + "Return value generator: "
				+ SAMPLE_RET_VAL_GEN_NAME + NEW_LINE + "Result: %s" + NEW_LINE + ".";

		String output;
		String expected;
		Result result;

		result = new Result();
		output = presentation.formatTestResultEntry(SAMPLE_TEST_IDENTIFIER, new JUnitTestRunResult(result),
				SAMPLE_METHOD_IDENTIFIER, SAMPLE_RET_VAL_GEN_NAME);
		expected = String.format(genericExpected, "OK");
		assertEquals(expected, output);

		result = new JUnitCore().run(Request.method(SampleJUnitTestClass.class, "b"));
		output = presentation.formatTestResultEntry(SAMPLE_TEST_IDENTIFIER, new JUnitTestRunResult(result),
				SAMPLE_METHOD_IDENTIFIER, SAMPLE_RET_VAL_GEN_NAME);
		expected = String.format(genericExpected, "1 of 1 FAILED" + NEW_LINE + "Exception: java.lang.AssertionError");
		assertEquals(expected, output);
	}

	@Test
	public void testDatabaseResultPresentation() {
		IResultPresentation presentation = new DatabaseResultPresentation();
		presentation.setExecutionId(ExecutionIdFactory.parseShortExecutionId("EXEC"));

		final String genericExpected = "INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('EXEC', 'junit.framework.TestCase.test()', 'java.lang.Object.hashCode()', '%s', %s, %s, '%s');";

		String output;
		String expected;
		Result result;

		result = new Result();
		output = presentation.formatTestResultEntry(SAMPLE_TEST_IDENTIFIER, new JUnitTestRunResult(result),
				SAMPLE_METHOD_IDENTIFIER, SAMPLE_RET_VAL_GEN_NAME);
		expected = String.format(genericExpected, SAMPLE_RET_VAL_GEN_NAME, false, false, "");
		assertEquals(expected, output);

		result = new JUnitCore().run(Request.method(SampleJUnitTestClass.class, "b"));
		output = presentation.formatTestResultEntry(SAMPLE_TEST_IDENTIFIER, new JUnitTestRunResult(result),
				SAMPLE_METHOD_IDENTIFIER, SAMPLE_RET_VAL_GEN_NAME);
		expected = String.format(genericExpected, SAMPLE_RET_VAL_GEN_NAME, true, true, AssertionError.class.getName());
		assertEquals(expected, output);
	}

}
