package de.tum.in.niedermr.ta.runner.execution.environment;

import java.io.File;
import java.io.IOException;

import de.tum.in.niedermr.ta.core.common.util.FileUtility;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;

public class Environment implements EnvironmentConstants {
	public static final String getWithIndex(String fileName, int index) {
		return String.format(fileName, index);
	}

	public static final String getClasspathOfIndexedFiles(String genericPath, int startIndex, int length) {
		StringBuilder sB = new StringBuilder();

		for (int i = startIndex; i < startIndex + length; i++) {
			sB.append(Environment.getWithIndex(genericPath, i));
			sB.append(CP_SEP);
		}

		return sB.toString();
	}

	public static final String replaceWorkingFolder(String fileOrClasspath, String workingFolder) {
		return replacePrefixedPathPart(fileOrClasspath, FOLDER_WORKING_AREA, workingFolder);
	}

	public static final String replaceProgramFolder(String fileOrClasspath, String programFolder) {
		return replacePrefixedPathPart(fileOrClasspath, FOLDER_PROGRAM, programFolder);
	}

	public static final String replaceFolders(String fileOrClasspath, String programFolder, String workingFolder) {
		return replaceProgramFolder(replaceWorkingFolder(fileOrClasspath, workingFolder), programFolder);
	}

	protected static final String replacePrefixedPathPart(String fileOrClasspath, String toBeReplaced, String value) {
		String v = value;

		if (!(v.endsWith(PATH_SEPARATOR) || v.endsWith(PATH_SEPARATOR_ALTERNATIVE))) {
			v += PATH_SEPARATOR;
		}

		return fileOrClasspath.replace(toBeReplaced, v);
	}

	public static final String prefixFileInWorkingFolder(String file) {
		return FileUtility.prefixFileNameIfNotAbsolute(file, FOLDER_WORKING_AREA);
	}

	/**
	 * Needed for example when starting a process outside the working folder with a specified classpath (relative to the
	 * working folder).
	 * 
	 * @see #replaceFolders(String, String, String)
	 * @see #replaceWorkingFolder(String, String)
	 */
	public static final String prefixClasspathInWorkingFolder(String classpath) {
		String[] pathElements = classpath.split(CP_SEP);

		StringBuilder sB = new StringBuilder();

		for (String path : pathElements) {
			if (path.isEmpty()) {
				continue;
			} else {
				sB.append(prefixFileInWorkingFolder(path));
				sB.append(CP_SEP);
			}
		}

		return sB.toString();
	}

	public static final String makeClasspathCanonical(String classpath) throws IOException {
		String[] pathElements = classpath.split(CP_SEP);

		StringBuilder sB = new StringBuilder();

		for (String path : pathElements) {
			if (path.isEmpty()) {
				continue;
			} else {
				sB.append(getCanonicalPath(path));
				sB.append(CP_SEP);
			}
		}

		return sB.toString();
	}

	private static String getCanonicalPath(final String path) throws IOException {
		File file = new File(getPathWithoutWildcard(path));

		return file.getCanonicalPath()
				+ (path.endsWith(CLASSPATH_WILDCARD) ? (PATH_SEPARATOR + CLASSPATH_WILDCARD) : "");
	}

	public static final String getPathWithoutWildcard(final String path) {
		if (path.endsWith(CLASSPATH_WILDCARD)) {
			return path.substring(0, path.length() - 1);
		} else {
			return path;
		}
	}

	public static final String getGenericFilePathOfOutputResult(Configuration configuration) {
		if (configuration.getResultPresentation().getValue()
				.equals(ResultPresentationProperty.RESULT_PRESENTATION_DB)) {
			return FILE_OUTPUT_RESULT_SQL;
		} else {
			return FILE_OUTPUT_RESULT_TXT;
		}
	}
}
