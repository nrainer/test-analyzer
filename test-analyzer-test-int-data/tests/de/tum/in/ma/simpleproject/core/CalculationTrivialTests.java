package de.tum.in.ma.simpleproject.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.ma.simpleproject.core.Calculation;

public class CalculationTrivialTests
{
	@Test
	public void emptyAtBeginning()
	{
		assertEquals(0, new Calculation().getResult());
	}
}
