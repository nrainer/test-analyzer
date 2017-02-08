package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class MiscellaneousTests {
	private static Special special;

	@BeforeClass
	public static void setUp() {
		special = new Special();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoReturnStatement() {
		special.noReturnStatement();
	}

	@Test
	public void testSyntheticBride() {
		assertEquals(5, special.compareTo(5));
		assertEquals(8, special.compareTo(8));
	}

	@Test
	public void testTryCatchBlock() {
		assertEquals(4, special.tryCatchBlock("4"));
		assertEquals(Integer.MAX_VALUE, special.tryCatchBlock("x"));
	}

	@Test
	public void testWithTryCatchBlock() {
		try {
			try {
				special.noReturnStatement();
			} catch (Exception ex) {
				throw ex;
			}
		} catch (Exception ex) {
			assertNotNull(ex);
		}
	}

	@Test
	public void testStaticMethod() {
		assertEquals(2, Special.staticMethod());
	}
}
