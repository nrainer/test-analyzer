package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Goal: only methods invoked from the test method are supposed to be logged.
 *
 */
public class SetUpAndTearDownTests
{
	private static final SpecialTest2Object obj = new SpecialTest2Object();
	
	@BeforeClass
	public static void beforeClass()
	{
		obj.callBeforeClass();
	}
	
	@Before
	public void before()
	{
		obj.callBefore();
	}
	
	@Test
	public void testBeforeAndAfterDontInstrument()
	{
		assertEquals(5, obj.callTest());
	}
	
	@After
	public void after()
	{
		obj.callAfter();
	}
	
	@AfterClass
	public static void afterClass()
	{
		obj.callAfterClass();
	}
}
