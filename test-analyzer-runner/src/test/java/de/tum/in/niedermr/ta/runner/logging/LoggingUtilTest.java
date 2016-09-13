package de.tum.in.niedermr.ta.runner.logging;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;

/** Test {@link LoggingUtil}. */
public class LoggingUtilTest {

	@Test
	public void testGetInputArgumentsF1() {
		ProgramArgsReader argsReader = new ProgramArgsReader(LoggingUtilTest.class, new String[] { "x", "y" });
		assertEquals(LoggingUtil.INPUT_ARGUMENTS_ARE + "[1] = y" + CommonConstants.SEPARATOR_DEFAULT,
				LoggingUtil.getInputArgumentsF1(argsReader));
	}

	@Test
	public void testShorten() {
		assertEquals("abc [...]", LoggingUtil.shorten(3, "abcd"));
		assertEquals("ab", LoggingUtil.shorten(3, "ab"));
	}
}
