package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Test {@link ResultReceiverFactory}. */
public class ResultReceiverFactoryTest extends AbstractFileResultReceiverTest {

	/** Test. */
	@Test
	public void testCreateFileReceiver() {
		assertTrue(ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(true,
				OUTPUT_FOLDER + "/file1") instanceof MultiFileResultReceiver);
		assertTrue(ResultReceiverFactory.createFileResultReceiverWithDefaultSettings(false,
				OUTPUT_FOLDER + "/file2") instanceof FileResultReceiver);
	}
}
