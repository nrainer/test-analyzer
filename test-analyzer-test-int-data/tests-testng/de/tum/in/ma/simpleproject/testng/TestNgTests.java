package de.tum.in.ma.simpleproject.testng;

import de.tum.in.ma.simpleproject.lite.CalculationLite;

public class TestNgTests
{
	@org.testng.annotations.Test
	public void testIsEven()
	{
		CalculationLite calculation = new CalculationLite();
		calculation.add(5);
		
		org.testng.Assert.assertFalse(calculation.isEven());
	}
	
	@org.testng.annotations.Test
	public void testIncrement()
	{
		CalculationLite calculation = new CalculationLite();
		calculation.increment();
		
		org.testng.Assert.assertEquals(1, calculation.getResult());
	}
	
	@org.testng.annotations.Test(enabled = false)
	public void ignoredTest()
	{
		new CalculationLite().increment();
	}
}
