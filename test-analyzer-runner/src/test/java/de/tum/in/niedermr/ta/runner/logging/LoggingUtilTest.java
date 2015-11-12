package de.tum.in.niedermr.ta.runner.logging;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

public class LoggingUtilTest {
	@Test
	public void testGetInputArgumentsF1() {
		assertEquals(LoggingUtil.INPUT_ARGUMENTS_ARE + "[1] = y" + CommonConstants.SEPARATOR_DEFAULT,
				LoggingUtil.getInputArgumentsF1(new String[] { "x", "y" }));
	}

	@Test
	public void testAppendPluralS() {
		assertEquals("1 error", LoggingUtil.appendPluralS(1, "error", true));
		assertEquals("2 errors", LoggingUtil.appendPluralS(2, "error", true));
		assertEquals("errors", LoggingUtil.appendPluralS(2, "error", false));
		assertEquals("0 items", LoggingUtil.appendPluralS(new LinkedList<>(), "item", true));
	}

	@Test
	public void testSingularOrPlural() {
		assertEquals("one", LoggingUtil.singularOrPlural(1, "one", "many", false));
		assertEquals("many", LoggingUtil.singularOrPlural(2, "one", "many", false));
		assertEquals("7 people", LoggingUtil.singularOrPlural(7, "person", "people", true));
		assertEquals("not one", LoggingUtil.singularOrPlural(new LinkedList<>(), "one", "not one", false));
	}

	@Test
	public void testShorten() {
		assertEquals("abc [...]", LoggingUtil.shorten(3, "abcd"));
		assertEquals("ab", LoggingUtil.shorten(3, "ab"));
	}
}
