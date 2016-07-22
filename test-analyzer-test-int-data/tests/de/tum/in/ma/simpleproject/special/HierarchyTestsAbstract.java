package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public abstract class HierarchyTestsAbstract
{
	protected final Special special = new Special();
	
	@Test
	public void testInAbstractClass()
	{
		assertEquals(0, special.add(0, 0, 0));
	}
}
