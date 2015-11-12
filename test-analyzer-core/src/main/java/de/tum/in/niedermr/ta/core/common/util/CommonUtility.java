package de.tum.in.niedermr.ta.core.common.util;

import java.util.Random;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

public class CommonUtility {
	public static final int LENGTH_OF_RANDOM_ID = 4;

	public static String getArgument(String[] args, int index) {
		return getArgument(args, index, "");
	}

	public static String getArgument(String[] args, int index, String alternative) {
		if (args != null && index < args.length) {
			String value = args[index];

			// needed for linux
			value = value.replace(CommonConstants.QUOTATION_MARK, "");

			return value;
		} else {
			return alternative;
		}
	}

	public static String createRandomId() {
		return createRandomId(LENGTH_OF_RANDOM_ID);
	}

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
}
