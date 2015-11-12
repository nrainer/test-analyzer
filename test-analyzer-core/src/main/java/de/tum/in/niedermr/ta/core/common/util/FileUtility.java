package de.tum.in.niedermr.ta.core.common.util;

import java.io.File;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public class FileUtility implements FileSystemConstants {
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
}
