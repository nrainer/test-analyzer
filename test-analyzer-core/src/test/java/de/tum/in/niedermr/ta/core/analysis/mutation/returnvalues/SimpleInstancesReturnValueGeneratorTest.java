package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class SimpleInstancesReturnValueGeneratorTest {
	@Test
	public void testCanHandleReturn() {
		IReturnValueGenerator retValGen = new SimpleInstancesReturnValueGenerator();

		assertTrue(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.getReturnType("()Ljava/lang/String;")));
		assertFalse(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.getReturnType("()[java/lang/Integer;")));
		assertFalse(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.getReturnType("()[java/lang/Collection;")));
	}
}
