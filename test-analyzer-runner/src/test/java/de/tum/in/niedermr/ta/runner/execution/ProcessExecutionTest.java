package de.tum.in.niedermr.ta.runner.execution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;

public class ProcessExecutionTest {

	@Test
	public void testWrapPattern() {
		assertFalse(StringUtility.isNullOrEmpty(ProcessExecution.wrapPattern(null)));
		assertFalse(StringUtility.isNullOrEmpty(ProcessExecution.wrapPattern("")));
		assertEquals("a" + CommonConstants.SEPARATOR_DEFAULT + "b",
				ProcessExecution.wrapPattern("a" + CommonConstants.SEPARATOR_DEFAULT + "b"));
	}

	@Test
	public void testUnwrapPattern() {
		assertArrayEquals(new String[0], ProcessExecution.unwrapAndSplitPattern(ProcessExecution.wrapPattern("")));
	}
}
