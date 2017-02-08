package de.tum.in.niedermr.ta.core.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

/** Test {@link FileUtility}. */
public class FileUtilityTest {

	/** Test. */
	@Test
	public void testPrefixFileNameIfNotAbsolute() {
		final String prefix = "./";
		String fileName;

		fileName = "a.jar";
		assertEquals(prefix + fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

		fileName = "./a.jar";
		assertEquals(prefix + fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

		fileName = "/E:/a.jar";
		assertEquals(fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

		if (CommonUtility.isRunningOnWindows()) {
			// further tests for Windows only

			fileName = "E:/a.jar";
			assertEquals(fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

			fileName = "E:\\a.jar";
			assertEquals(fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));
		}
	}

	/** Test. */
	@Test
	public void testEnsurePathEndsWithPathSeparator() {
		String pathSeparator = FileSystemConstants.PATH_SEPARATOR;

		assertEquals("", FileUtility.ensurePathEndsWithPathSeparator(null, pathSeparator));
		assertEquals("", FileUtility.ensurePathEndsWithPathSeparator("", pathSeparator));
		assertEquals("./src" + pathSeparator, FileUtility.ensurePathEndsWithPathSeparator("./src", pathSeparator));
		assertEquals("./src" + pathSeparator,
				FileUtility.ensurePathEndsWithPathSeparator("./src" + pathSeparator, pathSeparator));
	}

	/** Test. */
	@Test
	public void testEndsWithPathSeparator() {
		assertTrue(FileUtility.endsWithPathSeparator("F:/"));
		assertTrue(FileUtility.endsWithPathSeparator("F:\\"));
		assertFalse(FileUtility.endsWithPathSeparator("F:/folder/element"));
	}
}
