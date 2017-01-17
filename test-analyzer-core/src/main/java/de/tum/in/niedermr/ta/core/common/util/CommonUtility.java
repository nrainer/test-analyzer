package de.tum.in.niedermr.ta.core.common.util;

import java.util.Random;

public class CommonUtility {
	public static String createRandomId(int length) {
		Random rd = new Random();

		StringBuilder sB = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int x = rd.nextInt(36);

			if (x < 10) {
				sB.append(x);
			} else {
				sB.append((char) (65 - 10 + x));
			}
		}

		return sB.toString();
	}

	/** Get the duration in seconds. */
	public static long getDuration(long startTimeInMs) {
		return (System.currentTimeMillis() - startTimeInMs) / 1000;
	}
}
