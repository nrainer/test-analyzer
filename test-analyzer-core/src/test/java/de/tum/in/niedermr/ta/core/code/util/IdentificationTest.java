package de.tum.in.niedermr.ta.core.code.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;

public class IdentificationTest implements JavaConstants {
	private static final String DESCRIPTOR_BIG_INTEGER = "Ljava/lang/BigInteger;";
	private static final String DESCRIPTOR_STRING = "Ljava/lang/String;";

	private static final String SAMPLE_CLASS_NAME = "org.test.Class";

	@Test
	public void testAsClassname() {
		assertEquals(SAMPLE_CLASS_NAME, Identification.asClassName(SAMPLE_CLASS_NAME.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR)));
	}

	@Test
	public void testIsString() {
		Type t;

		t = Type.getType(DESCRIPTOR_BIG_INTEGER);
		assertFalse(Identification.isString(t));

		t = Type.getType(DESCRIPTOR_STRING);
		assertTrue(Identification.isString(t));
	}
}
