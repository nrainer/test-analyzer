package de.tum.in.niedermr.ta.core.common.util;

import java.util.List;

public class StringUtility {
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

	public static String join(List<String> values, String separator) {
		return join(values.toArray(new String[0]), separator);
	}

	public static String join(String[] values, String separator) {
		if (values.length == 0) {
			return "";
		}

		StringBuilder sB = new StringBuilder();

		for (String v : values) {
			sB.append(separator);
			sB.append(v);
		}

		return sB.toString().substring(separator.length());
	}
}
