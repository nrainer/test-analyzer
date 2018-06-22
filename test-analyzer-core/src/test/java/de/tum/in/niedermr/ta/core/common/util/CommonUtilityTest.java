package de.tum.in.niedermr.ta.core.common.util;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Test;

public class CommonUtilityTest {

	/** Test. */
	@Test
	public void testCreateRandomId() {
		final int length = 10;
		assertEquals(length, CommonUtility.createRandomId(length).length());
	}

	/** Test. */
	@Test
	public void testCreateDateTimeStringForFile() {
		Instant instant = Instant.parse("2007-12-03T10:15:30.00Z");
		assertEquals("20071203111530", CommonUtility.createDateTimeStringForFile(instant));
	}
}
