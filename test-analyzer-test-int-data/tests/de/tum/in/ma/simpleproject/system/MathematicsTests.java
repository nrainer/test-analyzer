package de.tum.in.ma.simpleproject.system;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.ma.simpleproject.core.Calculation;
import de.tum.in.ma.simpleproject.system.Mathematics;

public class MathematicsTests
{
	@Test
	public void testFaculty1()
	{
		Calculation c;
		
		c = new Calculation(1);
		Mathematics.faculty(c);
		assertEquals(1, c.getResult());
		
		c = new Calculation(5);
		Mathematics.faculty(c);
		assertEquals(120, c.getResult());
		
		c = Calculation.parse("3");
		Mathematics.faculty(c);
		assertEquals(6, c.getResult());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFaculty2()
	{
		Mathematics.faculty(new Calculation(-1));
	}
}
