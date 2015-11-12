package de.tum.in.niedermr.ta.core.code.identifier;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;

public class MethodIdentifierTest {
	private static final String CLASS_NAME_BIG_INTEGER = "java.lang.BigInteger";
	private static final String DESCRIPTOR_BIG_INTEGER = "Ljava/lang/BigInteger;";

	private static final String SAMPLE_CLASS_NAME = "org.test.Class";
	private static final String SAMPLE_METHOD_NAME = "execute";
	private static final String SAMPLE_ARGUMENTS_1 = "(boolean,int,int)";
	private static final String SAMPLE_ARGUMENTS_2 = "(" + SAMPLE_CLASS_NAME + ")";
	private static final String SAMPLE_DESCRIPTOR = "(ZII)" + DESCRIPTOR_BIG_INTEGER;

	private static final String EXPECTED_IDENTIFIER_STRING = "org.test.Class.execute(boolean,int,int)";
	private static final String EXPECTED_RETURN_TYPE_STRING = CLASS_NAME_BIG_INTEGER;
	private static final String EXPECTED_IDENTIFIER_WITH_RETURN_TYPE = EXPECTED_IDENTIFIER_STRING
			+ JavaConstants.RETURN_TYPE_SEPARATOR + EXPECTED_RETURN_TYPE_STRING;

	@Test
	public void testCreate1() {
		MethodIdentifier identifier = MethodIdentifier.create(SAMPLE_CLASS_NAME, SAMPLE_METHOD_NAME, SAMPLE_DESCRIPTOR);

		assertEquals(EXPECTED_IDENTIFIER_STRING, identifier.get());
		assertEquals(EXPECTED_IDENTIFIER_WITH_RETURN_TYPE, identifier.getWithReturnType());
		assertEquals(EXPECTED_RETURN_TYPE_STRING, identifier.getOnlyReturnType());
	}

	@Test
	public void testCreate2() {
		MethodIdentifier identifier1 = MethodIdentifier.create(SAMPLE_CLASS_NAME, SAMPLE_METHOD_NAME,
				SAMPLE_DESCRIPTOR);
		MethodIdentifier identifier2 = MethodIdentifier.create(
				SAMPLE_CLASS_NAME.replace(JavaConstants.PACKAGE_SEPARATOR, JavaConstants.PATH_SEPARATOR),
				SAMPLE_METHOD_NAME, SAMPLE_DESCRIPTOR);

		assertEquals(identifier1, identifier2);
	}

	@Test(expected = Exception.class)
	public void testCreateWithWrongDescriptor() {
		MethodIdentifier.create(SAMPLE_CLASS_NAME, SAMPLE_METHOD_NAME, "(text.String)V");
	}

	@Test
	public void testParse1() {
		MethodIdentifier identifier = MethodIdentifier.create(SAMPLE_CLASS_NAME, SAMPLE_METHOD_NAME, SAMPLE_DESCRIPTOR);

		assertEquals(identifier, MethodIdentifier.parse(identifier.get()));
		assertEquals(identifier, MethodIdentifier.parse(identifier.getWithReturnType()));
		assertEquals(identifier.getWithReturnType(),
				MethodIdentifier.parse(identifier.getWithReturnType()).getWithReturnType());
	}

	@Test
	public void testParse2() {
		MethodIdentifier expected = MethodIdentifier.create(SAMPLE_CLASS_NAME, SAMPLE_METHOD_NAME, SAMPLE_DESCRIPTOR);
		MethodIdentifier identifier2 = MethodIdentifier.parse(
				SAMPLE_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + SAMPLE_METHOD_NAME + SAMPLE_ARGUMENTS_1);

		assertEquals(expected.get(), identifier2.get());
		assertEquals(expected.get() + JavaConstants.RETURN_TYPE_SEPARATOR + "?", identifier2.getWithReturnType());
	}

	@Test
	public void testEquals() {
		MethodIdentifier identifier1 = MethodIdentifier.parse(
				SAMPLE_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + SAMPLE_METHOD_NAME + SAMPLE_ARGUMENTS_1);
		MethodIdentifier identifier2 = MethodIdentifier
				.parse(SAMPLE_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + SAMPLE_METHOD_NAME
						+ SAMPLE_ARGUMENTS_1 + JavaConstants.RETURN_TYPE_SEPARATOR + CLASS_NAME_BIG_INTEGER);

		assertEquals(identifier1, identifier2);
	}

	@Test
	public void testGetOnlyClassName1() {
		MethodIdentifier identifier;

		identifier = MethodIdentifier.parse(
				SAMPLE_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + SAMPLE_METHOD_NAME + SAMPLE_ARGUMENTS_1);
		assertEquals(SAMPLE_CLASS_NAME, identifier.getOnlyClassName());

		identifier = MethodIdentifier.parse(Map.Entry.class.getName() + JavaConstants.CLASS_METHOD_SEPARATOR
				+ SAMPLE_METHOD_NAME + SAMPLE_ARGUMENTS_1);
		assertEquals(Map.Entry.class.getName(), identifier.getOnlyClassName());

		identifier = MethodIdentifier.parse(
				SAMPLE_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + SAMPLE_METHOD_NAME + SAMPLE_ARGUMENTS_2);
		assertEquals(SAMPLE_CLASS_NAME, identifier.getOnlyClassName());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetOnlyClassName2() {
		MethodIdentifier.EMPTY.getOnlyClassName();
	}

	@Test
	public void testGetOnlyMethodName() {
		MethodIdentifier identifier;

		identifier = MethodIdentifier.parse(
				SAMPLE_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + SAMPLE_METHOD_NAME + SAMPLE_ARGUMENTS_1);
		assertEquals(SAMPLE_METHOD_NAME, identifier.getOnlyMethodName());

		identifier = MethodIdentifier.parse(
				SAMPLE_CLASS_NAME + JavaConstants.CLASS_METHOD_SEPARATOR + SAMPLE_METHOD_NAME + SAMPLE_ARGUMENTS_2);
		assertEquals(SAMPLE_METHOD_NAME, identifier.getOnlyMethodName());
	}
}
