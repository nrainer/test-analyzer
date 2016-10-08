package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.io.TextFileData;

/** Test {@link FileResultReceiver}. */
public class MultiFileResultReceiverTest extends AbstractFileResultReceiverTest {

	protected static final String OUTPUT_FILE_NAME_PATTERN = OUTPUT_FOLDER + "result.txt";

	/** Test. */
	@Test
	public void testReceiver() throws IOException {
		MultiFileResultReceiver receiver = new MultiFileResultReceiver(OUTPUT_FILE_NAME_PATTERN, 5);
		assertEquals(1, receiver.getFileCount());
		assertEquals(new File(OUTPUT_FOLDER + "chunk-1-result.txt").getPath(), receiver.getFileName(1));

		receiver.append("1");
		assertEquals(1, receiver.getFileCount());

		receiver.append(Arrays.asList("2", "3", "4"));
		receiver.markResultAsPartiallyComplete();
		assertEquals(1, receiver.getFileCount());

		receiver.append(Arrays.asList("5", "6"));
		assertEquals(1, receiver.getFileCount());

		receiver.markResultAsPartiallyComplete();
		receiver.append("7");
		assertEquals(2, receiver.getFileCount());

		receiver.markResultAsComplete();

		assertEquals(6, TextFileData.readFromFile(receiver.getFileName(1)).size());
		assertEquals(1, TextFileData.readFromFile(receiver.getFileName(2)).size());
	}

	/** Test. */
	@Test
	public void testGetFileName() {
		assertEquals(new File("/result/chunk-5-output.sql.txt").getPath(),
				MultiFileResultReceiver.getFileName("/result/output.sql.txt", 5));
		assertEquals("chunk-5-data.txt", MultiFileResultReceiver.getFileName("data.txt", 5));
	}
}
