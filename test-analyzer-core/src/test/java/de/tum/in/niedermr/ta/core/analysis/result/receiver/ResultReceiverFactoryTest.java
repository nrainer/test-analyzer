package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Test {@link ResultReceiverFactory}. */
public class ResultReceiverFactoryTest {

	/** Test. */
	@Test
	public void testCreateFileReceiver() {
		assertTrue(ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(true,
				"a") instanceof MultiFileResultReceiver);
		assertTrue(ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(false,
				"a") instanceof FileResultReceiver);
	}
}
