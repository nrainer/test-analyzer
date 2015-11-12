package de.tum.in.niedermr.ta.core.analysis.instrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger.LoggingMode;

public class InvocationLoggerTest {
	@Before
	public void setUp() {
		InvocationLogger.reset();
	}

	@Test
	public void testTestingLogIsDefault() {
		InvocationLogger.setMode(LoggingMode.TESTING);
		InvocationLogger.reset();

		InvocationLogger.log("f");

		assertEquals(0, InvocationLogger.getTestingLog().size());
		assertEquals(1, InvocationLogger.getFramingLog().size());
	}

	@Test
	public void testLogging() {
		assertIsClean();

		InvocationLogger.setMode(LoggingMode.TESTING);

		InvocationLogger.log("t1");
		InvocationLogger.log("t2");

		InvocationLogger.setMode(LoggingMode.FRAMING);

		InvocationLogger.log("f1");

		InvocationLogger.setMode(LoggingMode.TESTING);

		InvocationLogger.log("t3");

		InvocationLogger.setMode(LoggingMode.TESTING);

		InvocationLogger.log("t4");

		InvocationLogger.setMode(LoggingMode.FRAMING);

		InvocationLogger.log("f2");

		assertEquals(4, InvocationLogger.getTestingLog().size());
		assertEquals(2, InvocationLogger.getFramingLog().size());

		for (String s : InvocationLogger.getTestingLog()) {
			assertTrue(s.startsWith("t"));
		}

		for (String s : InvocationLogger.getFramingLog()) {
			assertTrue(s.startsWith("f"));
		}
	}

	@Test
	public void testReset() {
		assertIsClean();

		InvocationLogger.log("t1");
		InvocationLogger.setMode(LoggingMode.FRAMING);
		InvocationLogger.log("f1");

		InvocationLogger.reset();

		assertIsClean();
	}

	private void assertIsClean() {
		assertTrue(InvocationLogger.getFramingLog().isEmpty());
		assertTrue(InvocationLogger.getTestingLog().isEmpty());
	}
}
