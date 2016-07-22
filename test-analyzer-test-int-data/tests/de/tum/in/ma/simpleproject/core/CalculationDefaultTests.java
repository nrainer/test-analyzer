package de.tum.in.ma.simpleproject.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.tum.in.ma.simpleproject.core.Calculation;

public class CalculationDefaultTests
{
	private Calculation calc;

	@Before
	public void setUp()
	{
		this.calc = new Calculation();
	}

	@Test
	public void constructor()
	{
		Calculation c2 = new Calculation();

		assertEquals(0, c2.getResult());

		c2 = new Calculation(5);

		assertEquals(5, c2.getResult());
	}

	@Test
	public void positive()
	{
		assertEquals(0, calc.getResult());
		assertTrue(calc.isPositive());

		calc.add(1);

		assertTrue(calc.isPositive());

		calc.increment();

		assertTrue(calc.isPositive());

		calc.mult(-1);

		assertFalse(calc.isPositive());
	}

	@Test
	public void even()
	{
		Calculation c = new Calculation(7);

		assertFalse(c.isEven());

		c.increment();

		assertTrue(c.isEven());
	}

	@Test
	public void add0()
	{
		assertEquals(0, calc.getResult());

		calc.add(0);

		assertEquals(0, calc.getResult());
	}

	@Test
	public void add5()
	{
		assertEquals(0, calc.getResult());

		calc.add(5);

		assertEquals(5, calc.getResult());
	}

	@Test
	public void mult1()
	{
		calc.add(1);

		assertEquals(1, calc.getResult());

		calc.mult(5);

		assertEquals(5, calc.getResult());
	}

	@Test
	public void mult2()
	{
		calc.add(1);

		calc.mult(0);

		assertEquals(0, calc.getResult());
	}

	@Test
	public void mult3()
	{
		calc.add(7);

		calc.mult(-2);

		assertEquals(-14, calc.getResult());
	}

	@Test
	public void intArray()
	{
		int[] result = calc.getResultAsArray();

		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(0, result[0]);
	}

	@Test
	public void increment()
	{
		assertEquals(0, calc.getResult());

		calc.increment();

		assertEquals(1, calc.getResult());
	}

	@Test
	public void clear()
	{
		assertEquals(0, calc.getResult());

		calc.add(5);

		assertEquals(5, calc.getResult());

		calc.clear();

		assertEquals(0, calc.getResult());
	}
	
	@Test
	public void sub()
	{
		calc.add(5);
		calc.sub(6);
		
		assertEquals(-1, calc.getResult());
	}
	
	@Test
	public void resultAsList()
	{
		calc.add(0);
		List<Integer> result = calc.getResultAsList();
		
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(new Integer(0), result.get(0));
	}
	
	@Test
	public void comparable()
	{
		assertEquals(0, calc.getComparable().compareTo(calc));
	}
}
