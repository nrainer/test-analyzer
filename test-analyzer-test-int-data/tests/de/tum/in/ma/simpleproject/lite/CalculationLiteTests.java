package de.tum.in.ma.simpleproject.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tum.in.ma.simpleproject.lite.CalculationLite;

public class CalculationLiteTests
{
	private CalculationLite calcLite;

	@Before
	public void setUp()
	{
		this.calcLite = new CalculationLite();
	}

	@Test
	public void emptyAtBeginning()
	{
		assertEquals(0, calcLite.getResult());
	}

	@Test
	public void even()
	{
		CalculationLite c = new CalculationLite();
		
		c.add(7);

		assertFalse(c.isEven());

		c.increment();

		assertTrue(c.isEven());
	}

	@Test
	public void add0()
	{
		assertEquals(0, calcLite.getResult());

		calcLite.add(0);

		assertEquals(0, calcLite.getResult());
	}

	@Test
	public void add5()
	{
		assertEquals(0, calcLite.getResult());

		calcLite.add(5);

		assertEquals(5, calcLite.getResult());
	}

	@Test
	public void increment()
	{
		assertEquals(0, calcLite.getResult());

		calcLite.increment();

		assertEquals(1, calcLite.getResult());
	}

	@Test
	public void stringCorrect()
	{
		assertEquals(String.valueOf(calcLite.getResult()), calcLite.getResultAsString());
	}
}
