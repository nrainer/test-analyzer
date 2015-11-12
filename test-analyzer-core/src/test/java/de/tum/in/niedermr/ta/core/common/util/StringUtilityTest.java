package de.tum.in.niedermr.ta.core.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

public class StringUtilityTest {
	@Test
	public void testNullOrEmpty() {
		assertTrue(StringUtility.isNullOrEmpty(null));
		assertTrue(StringUtility.isNullOrEmpty(""));
		assertTrue(StringUtility.isNullOrEmpty(" "));
		assertFalse(StringUtility.isNullOrEmpty("A"));
	}

	@Test
	public void testJoin() {
		assertEquals("", StringUtility.join(new String[0], ";"));
		assertEquals("", StringUtility.join(new LinkedList<String>(), ";"));

		assertEquals("A", StringUtility.join(new String[] { "A" }, ";"));
		assertEquals("A;B", StringUtility.join(new String[] { "A", "B" }, ";"));

		assertEquals("AB", StringUtility.join(new String[] { "A", "B" }, ""));
	}
}
