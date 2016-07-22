package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HasFailingTest
{
	@Test
	public void successfulTest()
	{
		assertEquals(2, Special.staticMethod());
	}
	
	@Test
	public void failingTest()
	{
		assertEquals(0, Special.staticMethod());
	}
}
