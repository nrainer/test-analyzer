package de.tum.in.niedermr.ta.core.code.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;

/** Test {@link TestcaseIdentifier}. */
public class TestcaseIdentifierTest {
	private static final String TEST_CASE_NAME = "testX";

	private static final Class<?> EXPECTED_TEST_IDENTIFIER_CLASS = TestcaseIdentifierTest.class;
	private static final String EXPECTED_TEST_IDENTIFIER_CLASS_NAME = EXPECTED_TEST_IDENTIFIER_CLASS.getName();
	private static final String EXPECTED_TEST_IDENTIFIER_STRING = EXPECTED_TEST_IDENTIFIER_CLASS_NAME
			+ TestcaseIdentifier.SEPARATOR + TEST_CASE_NAME;

	/** Test. */
	@Test
	public void testCreate() throws ClassNotFoundException {
		TestcaseIdentifier testIdentifier = TestcaseIdentifier.create(EXPECTED_TEST_IDENTIFIER_CLASS, TEST_CASE_NAME);

		assertEquals(EXPECTED_TEST_IDENTIFIER_CLASS, testIdentifier.resolveTestClass());
		assertEquals(EXPECTED_TEST_IDENTIFIER_CLASS_NAME, testIdentifier.getTestClassName());
		assertEquals(TEST_CASE_NAME, testIdentifier.getTestcaseName());
		assertEquals(EXPECTED_TEST_IDENTIFIER_STRING, testIdentifier.get());
	}

	/** Test. */
	@Test
	public void testCreateFromJavaName() {
		TestcaseIdentifier testIdentifier = TestcaseIdentifier.createFromJavaName(
				EXPECTED_TEST_IDENTIFIER_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + TEST_CASE_NAME);
		assertEquals(EXPECTED_TEST_IDENTIFIER_CLASS_NAME, testIdentifier.getTestClassName());
		assertEquals(TEST_CASE_NAME, testIdentifier.getTestcaseName());
	}

	/** Test. */
	@Test
	public void testCreateForNonCodeTestcase() throws ClassNotFoundException {
		String testDisplayName = "comment_attribution_scenarios.story";

		TestcaseIdentifier testIdentifier = TestcaseIdentifier.createForNonCodeTestcase(testDisplayName);
		assertEquals(TestcaseIdentifier.NON_CODE_TEST_CLASS_NAME, testIdentifier.getTestClassName());
		assertEquals(testDisplayName, testIdentifier.getTestcaseName());
		assertNull(testIdentifier.resolveTestClass());
	}

	/** Test. */
	@Test
	public void testParse() throws ClassNotFoundException {
		TestcaseIdentifier testIdentifier = TestcaseIdentifier.create(TestcaseIdentifierTest.class, TEST_CASE_NAME);

		assertEquals(testIdentifier, TestcaseIdentifier.parse(testIdentifier.get()));
	}

	/** Test. */
	@Test
	public void testToMethodIdentifier() throws ClassNotFoundException {
		TestcaseIdentifier testIdentifier = TestcaseIdentifier.create(TestcaseIdentifierTest.class, TEST_CASE_NAME);
		MethodIdentifier methodIdentifier = MethodIdentifier.create(TestcaseIdentifierTest.class, TEST_CASE_NAME,
				BytecodeConstants.DESCRIPTOR_NO_PARAM_AND_VOID);

		assertEquals(methodIdentifier, testIdentifier.toMethodIdentifier());
	}

	/** Test. */
	@Test
	public void testSanitizeName() throws ClassNotFoundException {
		TestcaseIdentifier testIdentifier = TestcaseIdentifier.createForNonCodeTestcase("This |here|");
		assertEquals("zzz.NonCodeTest;This here", testIdentifier.get());
	}
}
