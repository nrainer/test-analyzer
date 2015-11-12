package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class CommonInstancesReturnValueGeneratorTest {
	private static final String DESCRIPTOR_STRING_ARRAY = "[Ljava.lang.String;";
	private static final String CLASS_NAME_STRING_ARRAY = "java.lang.String[]";

	@Test
	public void testSupports() throws Exception {
		CommonInstancesReturnValueGenerator retValGen = new CommonInstancesReturnValueGenerator();

		assertTrue(CommonFactory.INSTANCE.supports(MethodIdentifier.EMPTY, CLASS_NAME_STRING_ARRAY));
		assertTrue(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.getType(DESCRIPTOR_STRING_ARRAY)));
	}
}
