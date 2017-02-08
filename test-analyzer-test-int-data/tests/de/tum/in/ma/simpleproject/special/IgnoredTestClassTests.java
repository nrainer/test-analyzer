package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class IgnoredTestClassTests {
	@Test
	public void testInIgnoredClass() {
		assertEquals(5, new Special().add(0, 5, 0));
	}
}
