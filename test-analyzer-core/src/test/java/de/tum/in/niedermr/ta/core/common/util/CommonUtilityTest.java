package de.tum.in.niedermr.ta.core.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

public class CommonUtilityTest {
	@Test
	public void testGetArgument() {
		String[] args1 = new String[] { "A" };
		String[] args2 = new String[] { CommonConstants.QUOTATION_MARK + "B" + CommonConstants.QUOTATION_MARK };

		assertEquals("A", CommonUtility.getArgument(args1, 0, "X"));
		assertEquals("X", CommonUtility.getArgument(args1, 1, "X"));
		assertEquals("B", CommonUtility.getArgument(args2, 0, "X"));
		assertEquals("X", CommonUtility.getArgument(null, 0, "X"));

		assertEquals("", CommonUtility.getArgument(null, 0));
	}

	@Test
	public void testCreateRandomId() {
		final int length = 10;
		assertEquals(length, CommonUtility.createRandomId(length).length());
	}
}
