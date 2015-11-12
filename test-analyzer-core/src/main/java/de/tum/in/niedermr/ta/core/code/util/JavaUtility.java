package de.tum.in.niedermr.ta.core.code.util;

import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public class JavaUtility implements JavaConstants {
	/**
	 * Returns the class file path with the file extension '.class'.
	 */
	public static String ensureClassFileEnding(String classFilePath) {
		if (!classFilePath.endsWith(FileSystemConstants.FILE_EXTENSION_CLASS)) {
			return classFilePath + FileSystemConstants.FILE_EXTENSION_CLASS;
		} else {
			return classFilePath;
		}
	}

	/**
	 * Returns the class file path without the file extension '.class'.
	 */
	public static String removeClassFileEnding(String classFilePath) {
		if (classFilePath.endsWith(FileSystemConstants.FILE_EXTENSION_CLASS)) {
			return classFilePath.substring(0, classFilePath.indexOf(FileSystemConstants.FILE_EXTENSION_CLASS));
		}

		return classFilePath;
	}

	/**
	 * Converts a class path to a class name.
	 */
	public static String toClassName(String classPath) {
		return removeClassFileEnding(classPath).replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
	}

	/**
	 * Converts a class name to a class path and adds the file extension '.class'.
	 */
	public static String toClassPathWithEnding(String className) {
		return ensureClassFileEnding(getClassPath(className));
	}

	/**
	 * Converts a class name to a class path (without file extension).
	 */
	public static String toClassPathWithoutEnding(String className) {
		return removeClassFileEnding(getClassPath(className));
	}

	/**
	 * Converts a class name to a class path (without file extension).
	 */
	public static String toClassPathWithoutEnding(Class<?> cls) {
		return toClassPathWithoutEnding(cls.getName());
	}

	private static String getClassPath(String className) {
		return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
	}

	/**
	 * Returns whether a class inherits another class than object.
	 */
	public static boolean hasSuperClassOtherThanObject(ClassNode cn) {
		if (cn.superName == null) {
			return false;
		} else {
			return !toClassName(cn.superName).equals(Object.class.getName());
		}
	}

	public static boolean inheritsClass(ClassNode cn, String nameOfSuperClassToBeDetected) throws ClassNotFoundException {
		return inheritsClass(cn, Class.forName(nameOfSuperClassToBeDetected));
	}

	public static boolean inheritsClass(ClassNode cn, Class<?> superClassToBeDetected) throws ClassNotFoundException {
		return inheritsClass(Class.forName(toClassName(cn.name)), superClassToBeDetected);
	}

	public static boolean inheritsClass(Class<?> cls, Class<?> superClassToBeDetected) {
		try {
			Class<?> superClass = cls;

			while (superClass != null) {
				if (superClass == superClassToBeDetected) {
					return true;
				}

				superClass = superClass.getSuperclass();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public static boolean inheritsClassNoEx(ClassNode cn, Class<?> superClassToBeDetected, boolean printStackTrace) {
		try {
			return inheritsClass(cn, superClassToBeDetected);
		} catch (ClassNotFoundException ex) {
			if (printStackTrace) {
				ex.printStackTrace();
			}

			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T createInstance(String className) throws ReflectiveOperationException {
		return (T) Class.forName(className).newInstance();
	}
}
