package de.tum.in.niedermr.ta.core.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.TestUtility;

public class TextFileDataTest {
	private static final String TEST_FOLDER = TestUtility.getTestFolder(TextFileDataTest.class);
	private static final String FILE_TEST_READ = TEST_FOLDER + "text.txt";
	private static final String FILE_TEST_WRITE = TEST_FOLDER + "text_write.txt";
	private static final String FILE_TEST_OVERWRITE = TEST_FOLDER + "text_overwrite.txt";
	private static final String FILE_TEST_APPEND = TEST_FOLDER + "text_append.txt";
	private static final String FILE_TEST_MERGE_SRC_X = TEST_FOLDER + "text_merge_src_%s.txt";
	private static final String FILE_TEST_MERGE_SRC_1 = String.format(FILE_TEST_MERGE_SRC_X, 0);
	private static final String FILE_TEST_MERGE_SRC_2 = String.format(FILE_TEST_MERGE_SRC_X, 1);
	private static final String FILE_TEST_MERGE_DEST = TEST_FOLDER + "text_merge_dest.txt";

	private static final String[] CLEAN_UP = new String[] { FILE_TEST_WRITE, FILE_TEST_APPEND, FILE_TEST_MERGE_SRC_1,
			FILE_TEST_MERGE_SRC_2, FILE_TEST_MERGE_DEST };

	@BeforeClass
	public static void setUp() {
		cleanUp();
	}

	@Test
	public void testReadFromFile() throws IOException {
		List<String> readLines = TextFileData.readFromFile(FILE_TEST_READ);

		assertEquals(3, readLines.size());

		assertEquals("Line 0", readLines.get(0));
		assertEquals("Line 1", readLines.get(1));
		assertEquals("Line 2", readLines.get(2));
	}

	@Test
	public void testWriteToFile() throws IOException {
		List<String> linesToWrite = new LinkedList<>();
		linesToWrite.add("A");
		linesToWrite.add("B");

		TextFileData.writeToFile(FILE_TEST_WRITE, linesToWrite);

		assertTrue(new File(FILE_TEST_WRITE).exists());

		List<String> readLines = TextFileData.readFromFile(FILE_TEST_WRITE);

		assertEquals(2, readLines.size());

		for (int i = 0; i < linesToWrite.size(); i++) {
			assertEquals(readLines.get(i), linesToWrite.get(i));
		}
	}

	@Test
	public void testOverwriteFile() throws IOException {
		List<String> linesToWrite1 = new LinkedList<>();
		linesToWrite1.add("D");

		TextFileData.writeToFile(FILE_TEST_OVERWRITE, linesToWrite1);

		List<String> linesToWrite2 = new LinkedList<>();
		linesToWrite2.add("E");

		TextFileData.writeToFile(FILE_TEST_OVERWRITE, linesToWrite2);

		List<String> readLines = TextFileData.readFromFile(FILE_TEST_OVERWRITE);

		assertEquals(1, readLines.size());
		assertEquals(readLines.get(0), linesToWrite2.get(0));
	}

	@Test
	public void testAppendToFile() throws IOException {
		List<String> linesToWrite = new LinkedList<>();
		linesToWrite.add("G");
		linesToWrite.add("H");

		TextFileData.writeToFile(FILE_TEST_APPEND, linesToWrite);

		List<String> linesToAppend = new LinkedList<>();
		linesToAppend.add("I");
		linesToAppend.add("J");

		TextFileData.appendToFile(FILE_TEST_APPEND, linesToAppend);

		List<String> readLines = TextFileData.readFromFile(FILE_TEST_APPEND);

		assertEquals(4, readLines.size());

		assertEquals(readLines.get(0), linesToWrite.get(0));
		assertEquals(readLines.get(2), linesToAppend.get(0));
	}

	@Test
	public void testMergeFiles() throws IOException {
		List<String> linesToWrite1 = new LinkedList<>();
		linesToWrite1.add("X1");
		linesToWrite1.add("X2");

		TextFileData.writeToFile(FILE_TEST_MERGE_SRC_1, linesToWrite1);

		List<String> linesToWrite2 = new LinkedList<>();
		linesToWrite2.add("Y1");
		linesToWrite2.add("Y2");

		TextFileData.writeToFile(FILE_TEST_MERGE_SRC_2, linesToWrite2);

		TextFileData.mergeFiles(FILE_TEST_MERGE_DEST, FILE_TEST_MERGE_SRC_X, 2);

		assertTrue(new File(FILE_TEST_MERGE_DEST).exists());

		List<String> readLines = TextFileData.readFromFile(FILE_TEST_MERGE_DEST);

		assertEquals(readLines.get(0), linesToWrite1.get(0));
		assertEquals(readLines.get(2), linesToWrite2.get(0));
	}

	@AfterClass
	public static void tearDown() {
		cleanUp();
	}

	private static void cleanUp() {
		for (String path : CLEAN_UP) {
			File file = new File(path);

			if (file.exists()) {
				file.delete();
			}
		}
	}
}
