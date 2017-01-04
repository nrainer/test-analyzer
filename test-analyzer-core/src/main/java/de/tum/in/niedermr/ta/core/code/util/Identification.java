package de.tum.in.niedermr.ta.core.code.util;

import org.objectweb.asm.Type;

public class Identification {
	/**
	 * Usage scenario: convert ClassNode.name to the class name
	 */
	public static String asClassName(String classNameOrPath) {
		return JavaUtility.toClassName(classNameOrPath);
	}

	public static boolean isPrimitiveOrString(Type type) {
		return isPrimitive(type) || isString(type);
	}

	public static boolean isString(Type type) {
		return String.class.getName().equals(type.getClassName());
	}

	public static boolean isVoid(Type type) {
		return type.getSort() == Type.VOID;
	}

	public static boolean isArray(Type type) {
		return type.getSort() == Type.ARRAY;
	}

	public static boolean isArrayOrObject(Type type) {
		return type.getSort() == Type.OBJECT || isArray(type);
	}

	public static boolean isPrimitive(Type type) {
		return type.getSort() >= Type.BOOLEAN && type.getSort() <= Type.DOUBLE;
	}

	public static String getMethodReturnType(String methodDesc) {
		return Type.getReturnType(methodDesc).getClassName();
	}
}
