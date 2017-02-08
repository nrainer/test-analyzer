package de.tum.in.niedermr.ta.runner.execution.environment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;

public class EnvironmentTest implements EnvironmentConstants, FileSystemConstants {
	/** Test. */
	@Test
	public void testGetWithIndex() {
		assertEquals("j_5.jar", Environment.getWithIndex("j_%s.jar", 5));
	}

	/** Test. */
	@Test
	public void testGetClasspathOfIndexedFiles() {
		assertEquals("j_5.jar" + CP_SEP + "j_6.jar" + CP_SEP, Environment.getClasspathOfIndexedFiles("j_%s.jar", 5, 2));
	}

	/** Test. */
	@Test
	public void testReplaceFolders() {
		String path = FOLDER_PROGRAM + "a.jar" + CP_SEP + PATH_WORKING_AREA_RESULT + "x.txt" + CP_SEP;
		String expected = "E:/a.jar" + CP_SEP + "F:/result/x.txt" + CP_SEP;

		assertEquals(expected, Environment.replaceFolders(path, "E:/", "F:/"));
	}

	/** Test. */
	@Test
	public void testReplacePrefixedPathPart() {
		assertEquals("C:/a.txt", Environment.replacePrefixedPathPart("{X}/a.txt", "{X}/", "C:" + PATH_SEPARATOR));
		assertEquals("C:/a.txt", Environment.replacePrefixedPathPart("{X}/a.txt", "{X}/", "C:"));
		assertEquals("C:\\a.txt",
				Environment.replacePrefixedPathPart("{X}/a.txt", "{X}/", "C:" + PATH_SEPARATOR_ALTERNATIVE));
	}

	/** Test. */
	@Test
	public void testPrefixFile() {
		assertEquals("", Environment.prefixFileInWorkingFolder(""));
		assertEquals("Absolute path", "/C:/a.jar", Environment.prefixFileInWorkingFolder("/C:/a.jar"));
		assertEquals(FOLDER_WORKING_AREA + "b.jar", Environment.prefixFileInWorkingFolder("b.jar"));

		if (CommonUtility.isRunningOnWindows()) {
			assertEquals("Absolute path", "C:/a.jar", Environment.prefixFileInWorkingFolder("C:/a.jar"));
		}
	}

	/** Test. */
	@Test
	public void testPrefixClasspath() {
		String classPath = "./a.jar" + CP_SEP + "./b.jar" + CP_SEP + "c.jar" + CP_SEP;
		String expected = FOLDER_WORKING_AREA + "./a.jar" + CP_SEP + FOLDER_WORKING_AREA + "./b.jar" + CP_SEP
				+ FOLDER_WORKING_AREA + "c.jar" + CP_SEP;

		assertEquals(expected, Environment.prefixClasspathInWorkingFolder(classPath));
	}
}
