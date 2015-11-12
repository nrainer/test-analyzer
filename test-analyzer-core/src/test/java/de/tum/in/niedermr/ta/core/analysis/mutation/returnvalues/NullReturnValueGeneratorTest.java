package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class NullReturnValueGeneratorTest {
	@Test
	public void testCanHandleReturn() {
		IReturnValueGenerator retValGen = new NullReturnValueGenerator();

		assertFalse(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.VOID_TYPE));
		assertFalse(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.BOOLEAN_TYPE));
		assertTrue(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.getReturnType("()Ljava/lang/String;")));
		assertTrue(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.getReturnType("()[I")));
	}
}
