package de.tum.in.niedermr.ta.runner.logging;

import java.util.Collection;

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
	 * Returns the parameter word with an appended character "s" if the size of the collection is 0 or greater than 1.
	 * 
	 * @param includeCountInResult
	 *            inserts the size of the collection in the result before the word
	 */
	public static String appendPluralS(Collection<?> collection, String word, boolean includeCountInResult) {
		return appendPluralS(collection.size(), word, includeCountInResult);
	}

	/**
	 * Returns the parameter word with an appended character "s" if count is 0 or greater than 1.
	 * 
	 * @param includeCountInResult
	 *            inserts the value of count in the result before the word
	 */
	public static String appendPluralS(int count, String word, boolean includeCountInResult) {
		return singularOrPlural(count, word, word + "s", includeCountInResult);
	}

	/**
	 * Returns singular if the size of the collection is exactly 1, otherwise plural.
	 * 
	 * @param includeCountInResult
	 *            inserts the size of the collection in the result before singular or plural
	 */
	public static String singularOrPlural(Collection<?> collection, String singular, String plural,
			boolean includeCountInResult) {
		return singularOrPlural(collection.size(), singular, plural, includeCountInResult);
	}

	/**
	 * Returns singular if count is exactly 1, otherwise plural.
	 * 
	 * @param includeCountInResult
	 *            inserts the value of count in the result before singular or plural
	 */
	public static String singularOrPlural(int count, String singular, String plural, boolean includeCountInResult) {
		return (includeCountInResult ? (count + " ") : "") + (count == 1 ? singular : plural);
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
