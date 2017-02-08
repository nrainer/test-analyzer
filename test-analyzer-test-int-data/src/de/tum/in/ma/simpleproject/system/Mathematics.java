package de.tum.in.ma.simpleproject.system;

import de.tum.in.ma.simpleproject.core.Calculation;

public class Mathematics {
	public static Calculation faculty(String n) {
		Calculation c = Calculation.parse(n);
		faculty(c);

		return c;
	}

	public static void faculty(Calculation c) {
		int n = c.getResult();

		if (n == 0) {
			c.mult(0);
		} else if (n < 0) {
			throw new IllegalArgumentException();
		}

		for (int i = n - 1; i > 1; i--) {
			c.mult(i);
		}
	}
}
