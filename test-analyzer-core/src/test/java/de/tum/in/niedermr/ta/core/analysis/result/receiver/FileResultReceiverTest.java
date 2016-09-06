package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.conqat.lib.commons.filesystem.FileSystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.TestUtility;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;

/** Test {@link FileResultReceiver}. */
public class FileResultReceiverTest {

	private static final String OUTPUT_FILE_NAME = TestUtility.getTestFolder(FileResultReceiverTest.class)
			+ "result.txt";

	@BeforeClass
	public static void beforeAll() throws IOException {
		FileSystemUtils.ensureDirectoryExists(new File(OUTPUT_FILE_NAME).getParentFile());
		cleanup();
	}

	@AfterClass
	public static void afterAll() {
		cleanup();
	}

	/** Test. */
	@Test
	public void testReceiver() throws IOException {
		FileResultReceiver receiver = new FileResultReceiver(OUTPUT_FILE_NAME, false, 5);
		assertTrue(receiver.isResultBufferEmpty());

		receiver.append(Arrays.asList("1", "2", "3", "4"));
		assertFalse(receiver.isResultBufferEmpty());

		receiver.append(Arrays.asList("5", "6"));
		assertTrue(receiver.isResultBufferEmpty());
		assertEquals(6, TextFileData.readFromFile(OUTPUT_FILE_NAME).size());

		receiver.append("7");
		assertFalse(receiver.isResultBufferEmpty());

		receiver.flush();
		assertTrue(receiver.isResultBufferEmpty());
		assertEquals(7, TextFileData.readFromFile(OUTPUT_FILE_NAME).size());

		// writes to the same file and resets the file at the beginning
		FileResultReceiver receiver2 = new FileResultReceiver(OUTPUT_FILE_NAME, true, 5);
		receiver2.append("X");
		receiver2.flush();
		assertEquals(1, TextFileData.readFromFile(OUTPUT_FILE_NAME).size());
	}

	/** Cleanup: remove the output file. */
	private static void cleanup() {
		File outputFile = new File(OUTPUT_FILE_NAME);

		if (outputFile.exists()) {
			outputFile.delete();
		}
	}
}
