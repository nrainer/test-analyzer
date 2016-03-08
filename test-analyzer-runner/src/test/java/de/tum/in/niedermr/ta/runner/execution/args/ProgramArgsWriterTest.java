package de.tum.in.niedermr.ta.runner.execution.args;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProgramArgsWriterTest {

	@Test
	public void testWriter() {
		ProgramArgsWriter writer = new ProgramArgsWriter(ProgramArgsReaderTest.PROGRAM_CLASS,
				ProgramArgsReaderTest.ARGS_COUNT);
		writer.setValue(ProgramArgsReaderTest.ARGS_0, "abc");

		String[] args = writer.getArgs();
		assertEquals(ProgramArgsReaderTest.ARGS_COUNT, args.length);
		assertEquals("abc", args[ProgramArgsReaderTest.ARGS_0.getIndex()]);
		assertEquals("", args[ProgramArgsReaderTest.ARGS_1.getIndex()]);
	}
}
