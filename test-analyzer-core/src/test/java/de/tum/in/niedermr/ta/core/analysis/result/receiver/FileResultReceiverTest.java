package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;

/** Test {@link FileResultReceiver}. */
public class FileResultReceiverTest extends AbstractFileResultReceiverTest {

	protected static final String OUTPUT_FILE_NAME = OUTPUT_FOLDER + "result.txt";

	/** Test. */
	@Test
	public void testReceiver() throws IOException {
		FileResultReceiver receiver = new FileResultReceiver(OUTPUT_FILE_NAME, false, 5);
		assertTrue(receiver.isResultBufferEmpty());

		receiver.append(Arrays.asList("1", "2", "3", "4"));
		assertFalse(receiver.isResultBufferEmpty());

		receiver.append(Arrays.asList("5", "6"));
		assertTrue(receiver.isResultBufferEmpty());
		assertEquals(6, TextFileUtility.readFromFile(OUTPUT_FILE_NAME).size());

		receiver.append("7");
		assertFalse(receiver.isResultBufferEmpty());

		receiver.flush();
		assertTrue(receiver.isResultBufferEmpty());
		assertEquals(7, TextFileUtility.readFromFile(OUTPUT_FILE_NAME).size());

		// writes to the same file and resets the file at the beginning
		FileResultReceiver receiver2 = new FileResultReceiver(OUTPUT_FILE_NAME, true, 5);
		receiver2.append("X");
		receiver2.flush();
		assertEquals(1, TextFileUtility.readFromFile(OUTPUT_FILE_NAME).size());
	}
}
