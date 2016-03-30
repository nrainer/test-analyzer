package de.tum.in.niedermr.ta.core.common.util;

import java.io.File;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

/** Utilities related to files. */
public class FileUtility implements FileSystemConstants {

	/** Prefix the file path if it is not absolute. */
	public static String prefixFileNameIfNotAbsolute(String fileName, String prefix) {
		if (fileName.isEmpty()) {
			return "";
		}

		File file = new File(fileName);

		if (file.isAbsolute()) {
			return fileName;
		} else {
			String result = fileName;

			if (result.startsWith(PATH_SEPARATOR)) {
				result = result.substring(1);
			}

			return prefix + result;
		}
	}

	/** Ensure that the path ends with a path separator. Returns the empty string if the path is null or empty. */
	public static String ensurePathEndsWithPathSeparator(String path, String pathSeparatorToAppend) {
		if (StringUtility.isNullOrEmpty(path)) {
			return "";
		}

		if (path.endsWith(FileSystemConstants.PATH_SEPARATOR)
				|| path.endsWith(FileSystemConstants.PATH_SEPARATOR_ALTERNATIVE)) {
			return path;
		}

		return path + pathSeparatorToAppend;
	}
}
