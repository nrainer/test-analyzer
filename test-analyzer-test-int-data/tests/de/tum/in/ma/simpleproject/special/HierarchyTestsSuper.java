package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HierarchyTestsSuper extends HierarchyTestsAbstract {
	@Test
	public void testInInheritableNonAbstractClass() {
		assertEquals(7, special.add(1, 4, 2));
	}
}
