package de.tum.in.niedermr.ta.core.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommonUtilityTest {

	@Test
	public void testCreateRandomId() {
		final int length = 10;
		assertEquals(length, CommonUtility.createRandomId(length).length());
	}
}
