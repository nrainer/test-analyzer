package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import java.io.File;
import java.io.IOException;

import org.conqat.lib.commons.filesystem.FileSystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import de.tum.in.niedermr.ta.core.common.TestUtility;

/** Base class for file based result receiver tests. */
public abstract class AbstractFileResultReceiverTest {
	protected static final String OUTPUT_FOLDER = TestUtility.getTestFolder(AbstractFileResultReceiverTest.class);

	@BeforeClass
	public static void beforeAll() throws IOException {
		cleanup();
		FileSystemUtils.ensureDirectoryExists(new File(OUTPUT_FOLDER));
	}

	@AfterClass
	public static void afterAll() {
		cleanup();
	}

	/** Cleanup: remove the output file. */
	private static void cleanup() {
		File outputFolder = new File(OUTPUT_FOLDER);

		if (outputFolder.exists()) {
			FileSystemUtils.deleteRecursively(outputFolder);
		}
	}
}
