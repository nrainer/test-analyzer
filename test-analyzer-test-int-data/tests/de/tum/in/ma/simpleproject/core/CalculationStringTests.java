package de.tum.in.ma.simpleproject.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.tum.in.ma.simpleproject.core.Calculation;

public class CalculationStringTests
{
	private Calculation calc;

	@Before
	public void setUp()
	{
		this.calc = new Calculation();
	}

	@Test
	public void stringReturnNotNull()
	{
		assertNotNull(calc.getResultAsString());
	}

	@Test
	public void stringCorrect()
	{
		assertEquals(String.valueOf(calc.getResult()), calc.getResultAsString());
	}
	
	@Test
	public void parse()
	{
		assertEquals(3, Calculation.parse("3").getResult());
		assertEquals(4, Calculation.parse("   4 ").getResult());
	}
}
