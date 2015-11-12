package de.tum.in.niedermr.ta.core.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FileUtilityTest {
	@Test
	public void testPrefixFileNameIfNotAbsolute() {
		final String prefix = "./";
		String fileName;

		fileName = "a.jar";
		assertEquals(prefix + fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

		fileName = "./a.jar";
		assertEquals(prefix + fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

		fileName = "E:/a.jar";
		assertEquals(fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

		fileName = "/E:/a.jar";
		assertEquals(fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));

		fileName = "E:\\a.jar";
		assertEquals(fileName, FileUtility.prefixFileNameIfNotAbsolute(fileName, prefix));
	}
}
