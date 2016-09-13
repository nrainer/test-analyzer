package de.tum.in.niedermr.ta.runner.logging;

import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;

public class LoggingUtil {
	private static final String MSG_DONT_START_THIS_CLASS = "Don't start %s! Use %s!";
	static final String INPUT_ARGUMENTS_ARE = "Input arguments are: ";

	public static String getInputArgumentsF1(ProgramArgsReader argsReader) {
		return getInputArguments(argsReader, 1);
	}

	public static String getInputArguments(ProgramArgsReader argsReader, int fromIndex) {
		StringBuilder logText = new StringBuilder();
		logText.append(INPUT_ARGUMENTS_ARE);
		logText.append(argsReader.toArgsInfoString(fromIndex));
		return logText.toString();
	}

	/**
	 * Shortens s and appends "[...]" if it is longer than the specified length.
	 */
	public static String shorten(int length, String s) {
		if (s == null || s.length() < length) {
			return s;
		} else {
			return s.substring(0, length) + " [...]";
		}
	}

	public static void printDontStartThisClass(Class<?> classNotToBeStarted, Class<?> alternativeClass) {
		printDontStartThisClass(classNotToBeStarted, alternativeClass.getName());
	}

	public static void printDontStartThisClass(Class<?> classNotToBeStarted, String alternative) {
		System.out.println(String.format(MSG_DONT_START_THIS_CLASS, classNotToBeStarted.getName(), alternative));
	}
}
