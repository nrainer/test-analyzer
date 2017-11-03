package de.tum.in.niedermr.ta.core.common.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public class ClasspathUtility {
	private static final String CHARACTER_SPACE = " ";
	private static final String ENCODED_CHARACTER_SPACE = "%20";

	public static String getCurrentClasspath() {
		StringBuilder sB = new StringBuilder();

		ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

		URL[] urlArray = ((URLClassLoader) sysClassLoader).getURLs();

		for (URL url : urlArray) {
			sB.append(url.getFile().replace(ENCODED_CHARACTER_SPACE, CHARACTER_SPACE));
			sB.append(FileSystemConstants.CP_SEP);
		}

		return sB.toString();
	}

	/**
	 * Warning: Must not be used when the program is started with the libraries to analyze, mutate and test.
	 */
	public static String getCurrentProgramClasspath() {
		String classpath = ClasspathUtility.getCurrentClasspath();

		if (JavaUtility.isClassAvailable("org.objectweb.asm.Opcodes")) {
			return classpath;
		}

		return classpath + ";/lib" + FileSystemConstants.CP_SEP;
	}

	/** Get the OS dependent classpath separator. */
	public static String getClasspathSeparator() {
		if (CommonUtility.isRunningOnWindows()) {
			return FileSystemConstants.CLASSPATH_SEPARATOR_WINDOWS;
		}

		return FileSystemConstants.CLASSPATH_SEPARATOR_LINUX;
	}

	public static URLClassLoader createClassLoader(String[] classpathElements) {
		return createClassLoader(Arrays.asList(classpathElements));
	}

	public static URLClassLoader createClassLoader(List<String> classpathElements) {
		List<URL> classpathUrls = new ArrayList<>();

		for (String element : classpathElements) {
			if (StringUtility.isNullOrEmpty(element)) {
				continue;
			}

			try {
				classpathUrls.add(new File(element).toURI().toURL());
			} catch (MalformedURLException e) {
				// should not happen
				throw new IllegalStateException("Malformed URL", e);
			}
		}

		return new URLClassLoader(classpathUrls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
	}
}
