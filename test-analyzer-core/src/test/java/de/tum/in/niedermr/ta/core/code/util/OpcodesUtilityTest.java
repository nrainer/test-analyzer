package de.tum.in.niedermr.ta.core.code.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class OpcodesUtilityTest implements Opcodes {
	@Test
	public void testIsMethodExitOpcode() {
		assertTrue(OpcodesUtility.isMethodExitOpcode(IRETURN));
		assertTrue(OpcodesUtility.isMethodExitOpcode(ATHROW));
		assertFalse(OpcodesUtility.isMethodExitOpcode(NOP));
	}

	@Test
	public void testIsXReturn() {
		assertTrue(OpcodesUtility.isXRETURN(RETURN));
		assertTrue(OpcodesUtility.isXRETURN(IRETURN));
		assertTrue(OpcodesUtility.isXRETURN(LRETURN));
		assertTrue(OpcodesUtility.isXRETURN(FRETURN));
		assertTrue(OpcodesUtility.isXRETURN(DRETURN));
		assertTrue(OpcodesUtility.isXRETURN(ARETURN));
		assertFalse(OpcodesUtility.isXRETURN(LOOKUPSWITCH));
		assertFalse(OpcodesUtility.isXRETURN(GETSTATIC));
	}

	@Test
	public void testHasFlag() {
		int access = ACC_PUBLIC + ACC_STATIC + ACC_FINAL;

		assertTrue(OpcodesUtility.hasFlag(access, ACC_PUBLIC));
		assertTrue(OpcodesUtility.hasFlag(access, ACC_STATIC));
		assertTrue(OpcodesUtility.hasFlag(access, ACC_FINAL));
		assertFalse(OpcodesUtility.hasFlag(access, ACC_ABSTRACT));
	}

	@Test
	public void testGetReturnOpcode() {
		assertEquals(RETURN, OpcodesUtility.getReturnOpcode(Type.getType("V")));
		assertEquals(IRETURN, OpcodesUtility.getReturnOpcode(Type.getType("I")));
		assertEquals(ARETURN, OpcodesUtility.getReturnOpcode(Type.getType("Ljava/lang/String;")));
	}
}
