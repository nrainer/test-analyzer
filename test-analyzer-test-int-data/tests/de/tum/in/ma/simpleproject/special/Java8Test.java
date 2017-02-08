package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Java8Test {
	@Test
	public void testMultiplyEight() {
		Java8 java8 = new Java8();
		assertEquals(3 * 8, java8.applyMultiplyWithEight(3));
	}

	@Test
	public void testPredicateLambda() {
		Java8 java8 = new Java8();
		assertEquals(6, java8.usePredicateLambda());
	}
}
