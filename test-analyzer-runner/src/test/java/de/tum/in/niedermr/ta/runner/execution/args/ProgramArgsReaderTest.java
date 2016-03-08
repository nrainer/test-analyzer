package de.tum.in.niedermr.ta.runner.execution.args;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;

public class ProgramArgsReaderTest {

	static final Class<?> PROGRAM_CLASS = AnalyzerRunnerStart.class;
	static final int ARGS_COUNT = 3;
	static final ProgramArgsKey ARGS_0 = new ProgramArgsKey(PROGRAM_CLASS, 0);
	static final ProgramArgsKey ARGS_1 = new ProgramArgsKey(PROGRAM_CLASS, 1);
	static final ProgramArgsKey ARGS_2 = new ProgramArgsKey(PROGRAM_CLASS, 2);
	static final ProgramArgsKey INVALID_ARGS_0 = new ProgramArgsKey(String.class, 0);
	static final ProgramArgsKey INVALID_ARGS_10 = new ProgramArgsKey(PROGRAM_CLASS, 10);

	@Test
	public void testReader() {
		String[] args = new String[ARGS_COUNT];
		args[0] = "abc";
		args[1] = "";
		args[2] = "x" + CommonConstants.QUOTATION_MARK + "y";

		ProgramArgsReader reader = new ProgramArgsReader(PROGRAM_CLASS, args);

		assertEquals(args[0], reader.getArgument(ARGS_0));
		assertEquals(args[0], reader.getArgumentUnsafe(0));
		assertEquals(args[1], reader.getArgument(ARGS_1));
		assertEquals("xyz", reader.getArgument(ARGS_1, "xyz"));
		assertEquals(args[2].replace(CommonConstants.QUOTATION_MARK, ""), reader.getArgument(ARGS_2));

		assertEquals("[0] = abc; [1] = ; [2] = x\"y;", reader.toArgsInfoString());
		assertEquals("[1] = ; [2] = x\"y;", reader.toArgsInfoString(1));
	}

	@Test
	public void testCheckValidProgramArgsKey() {
		try {
			ProgramArgsReader reader = new ProgramArgsReader(PROGRAM_CLASS, new String[ARGS_COUNT]);
			reader.checkProgramArgsKey(ARGS_0);
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetWithInvalidProgramArgs1() {
		ProgramArgsReader reader = new ProgramArgsReader(PROGRAM_CLASS, new String[ARGS_COUNT]);
		reader.getArgument(INVALID_ARGS_0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetWithInvalidProgramArgs2() {
		ProgramArgsReader reader = new ProgramArgsReader(PROGRAM_CLASS, new String[ARGS_COUNT]);
		reader.getArgument(INVALID_ARGS_10);
	}
}
