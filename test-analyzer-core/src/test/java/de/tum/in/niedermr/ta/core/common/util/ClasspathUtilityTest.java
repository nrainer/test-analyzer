package de.tum.in.niedermr.ta.core.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

/** Test {@link ClasspathUtility}. */
public class ClasspathUtilityTest {

	/** Test. */
	@Test
	public void testGetCurrentClasspath() {
		String currentClasspath = ClasspathUtility.getCurrentClasspath();
		assertNotNull(currentClasspath);
		assertFalse(currentClasspath.contains(" "));
		assertTrue(currentClasspath.endsWith(FileSystemConstants.CP_SEP));
	}
}
