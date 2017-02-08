package de.tum.in.ma.simpleproject.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculationTrivialTests {
	@Test
	public void emptyAtBeginning() {
		assertEquals(0, new Calculation(0).getResult());
	}
}
