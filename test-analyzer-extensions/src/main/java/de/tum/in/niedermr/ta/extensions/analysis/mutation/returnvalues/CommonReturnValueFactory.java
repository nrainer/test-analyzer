package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueFactory;
import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/**
 * Can be used as fallback factory. Note that the type string is supposed to be handled by a simple return value
 * generator on request.
 */
public class CommonReturnValueFactory extends AbstractReturnValueFactory {
	public static final CommonReturnValueFactory INSTANCE = new CommonReturnValueFactory();

	private static final String PCKG_JAVA = "java";
	private static final String PCKG_JAVA_LANG = PCKG_JAVA + JavaConstants.PACKAGE_SEPARATOR + "lang";
	private static final String PCKG_JAVA_UTIL = PCKG_JAVA + JavaConstants.PACKAGE_SEPARATOR + "util";
	private static final String PCKG_JAVA_IO = PCKG_JAVA + JavaConstants.PACKAGE_SEPARATOR + "io";
	private static final String PCKG_JAVA_MATH = PCKG_JAVA + JavaConstants.PACKAGE_SEPARATOR + "math";

	/** {@inheritDoc} */
	@Override
	protected Optional<AbstractReturnValueFactory> getConfiguredFallbackFactory() {
		return Optional.of(SimpleReflectionReturnValueFactory.INSTANCE);
	}

	/** {@inheritDoc} */
	@Override
	public Object createWithException(MethodIdentifier methodIdentifier, String returnType) throws Throwable {
		if (returnType.endsWith(JavaConstants.ARRAY_BRACKETS)) {
			if (returnType.startsWith(PCKG_JAVA_LANG)) {
				return createJavaLangArray(returnType);
			} else {
				return createOtherArray(returnType);
			}
		}

		if (returnType.startsWith(PCKG_JAVA_LANG)) {
			return createJavaLang(returnType);
		} else if (returnType.startsWith(PCKG_JAVA_UTIL)) {
			return createJavaUtil(returnType);
		} else if (returnType.startsWith(PCKG_JAVA_IO)) {
			return createJavaIO(returnType);
		} else if (returnType.startsWith(PCKG_JAVA_MATH)) {
			return createJavaMath(returnType);
		}
		return createOther(returnType);
	}

	private Object createJavaLangArray(String returnType) {
		switch (returnType) {
		case "java.lang.Object[]":
			return new Object[0];
		case "java.lang.String[]":
			return new String[0];
		case "java.lang.Boolean[]":
			return new Boolean[0];
		case "java.lang.Byte[]":
			return new Byte[0];
		case "java.lang.Character[]":
			return new Character[0];
		case "java.lang.Number[]":
		case "java.lang.Integer[]":
			return new Integer[0];
		case "java.lang.Long[]":
			return new Long[0];
		case "java.lang.Float[]":
			return new Float[0];
		case "java.lang.Double[]":
			return new Double[0];
		case "java.lang.Short[]":
			return new Short[0];
		default:
			throw new NoSuchElementException();
		}
	}

	private Object createOtherArray(String returnType) throws NoSuchElementException {
		switch (returnType) {
		case "int[]":
			return new int[0];
		case "int[][]":
			return new int[0][];
		case "byte[]":
			return new byte[0];
		case "char[]":
			return new char[0];
		case "double[]":
			return new double[0];
		case "double[][]":
			return new double[0][];
		case "boolean[]":
			return new boolean[0];
		case "float[]":
			return new float[0];
		case "short[]":
			return new short[0];
		case "long[]":
			return new long[0];
		default:
			throw new NoSuchElementException();
		}
	}

	private Object createJavaLang(String returnType) throws NoSuchElementException {
		// Note that wrapper classes and String are explicitly not supported because they are handled by the simple
		// return value generators.

		switch (returnType) {
		case "java.lang.Object":
			return new Object();
		case "java.lang.Number":
			return Integer.valueOf(0);
		case "java.lang.Class":
			return Object.class;
		case "java.lang.Comparable":
			return newComparable();
		case "java.lang.Iterable":
			return new LinkedList<>();
		case "java.lang.Throwable":
		case "java.lang.Exception":
			return new Exception();
		case "java.lang.CharSequence":
			return "";
		case "java.lang.StringBuffer":
			return new StringBuffer();
		case "java.lang.StringBuilder":
			return new StringBuilder();
		case "java.lang.ClassLoader":
			return this.getClass().getClassLoader();
		case "java.lang.StackTraceElement":
			return new Exception().getStackTrace()[0];
		default:
			throw new NoSuchElementException();
		}
	}

	private Object createJavaUtil(String returnType) throws NoSuchElementException {
		Object result;

		result = tryCreateJavaUtilIterable(returnType);

		if (result != null) {
			return result;
		}

		result = tryCreateJavaUtilNonIterable(returnType);

		if (result != null) {
			return result;
		}

		throw new NoSuchElementException();
	}

	private Object tryCreateJavaUtilIterable(String returnType) {
		switch (returnType) {
		case "java.util.Collection":
		case "java.util.List":
		case "java.util.Queue":
		case "java.util.LinkedList":
			return new LinkedList<>();
		case "java.util.ArrayList":
			return new ArrayList<>();
		case "java.util.Vector":
		case "java.util.Stack":
			return new Stack<>();
		case "java.util.Iterator":
		case "java.util.ListIterator":
			return new LinkedList<>().listIterator();
		case "java.util.Set":
		case "java.util.HashSet":
			return new HashSet<>();
		case "java.util.SortedSet":
			return new TreeSet<>();
		case "java.util.Map":
		case "java.util.HashMap":
			return new HashMap<>();
		case "java.util.Map$Entry":
			Map<String, Object> map = new HashMap<>();
			map.put("", new Object());
			return map.entrySet().iterator().next();
		case "java.util.SortedMap":
			return new TreeMap<>();
		default:
			return null;
		}
	}

	private Object tryCreateJavaUtilNonIterable(String returnType) throws NoSuchElementException {
		switch (returnType) {
		case "java.util.Optional":
			return Optional.empty();
		case "java.util.Date":
			return new Date(0);
		case "java.util.Comparator":
			return newComparator();
		case "java.util.Locale":
			return Locale.GERMAN;
		case "java.util.Calendar":
			return Calendar.getInstance();
		case "java.util.regex.Pattern":
			return Pattern.compile(".");
		case "java.util.concurrent.ThreadPoolExecutor":
			return new java.util.concurrent.ScheduledThreadPoolExecutor(1);
		case "java.util.Formatter":
			return new java.util.Formatter();
		default:
			return null;
		}
	}

	private Object createJavaIO(String returnType) throws NoSuchElementException {
		switch (returnType) {
		case "java.io.File":
			return new File("./files/textfile.txt");
		case "java.io.Serializable":
			return "";
		case "java.io.Reader":
			return new java.io.StringReader("a");
		default:
			throw new NoSuchElementException();
		}
	}

	private Object createJavaMath(String returnType) throws NoSuchElementException {
		switch (returnType) {
		case "java.math.BigInteger":
			return BigInteger.ZERO;
		case "java.math.BigDecimal":
			return BigDecimal.ZERO;
		case "java.math.RoundingMode":
			return RoundingMode.CEILING;
		default:
			throw new NoSuchElementException();
		}
	}

	private Object createOther(String returnType) throws Throwable {
		switch (returnType) {
		case "java.net.InetAddress":
			return java.net.InetAddress.getLoopbackAddress();
		case "java.net.URL":
			return new java.net.URL("http://www.google.com");
		case "java.nio.charset.Charset":
			return java.nio.charset.Charset.defaultCharset();
		case "java.text.Format":
		case "java.text.NumberFormat":
			return java.text.NumberFormat.getInstance();
		default:
			throw new NoSuchElementException();
		}
	}

	private Object newComparable() {
		return new Comparable<Object>() {
			@Override
			public int compareTo(Object o) {
				return 1;
			}
		};
	}

	private Object newComparator() {
		return new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return 1;
			}
		};
	}
}
