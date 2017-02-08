package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HierarchyTestsInherited extends HierarchyTestsSuper {
	@Test
	public void testInInheritingClass() {
		assertEquals(2, special.add(1, 0, 1));
	}
}
