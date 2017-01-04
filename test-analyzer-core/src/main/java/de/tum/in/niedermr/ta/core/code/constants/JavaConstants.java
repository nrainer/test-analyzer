package de.tum.in.niedermr.ta.core.code.constants;

import java.util.HashSet;
import java.util.Set;

/** Java constants. */
public final class JavaConstants {

	/** Constructor. */
	private JavaConstants() {
		// NOP
	}

	/** {@value} */
	public static final String PATH_SEPARATOR = "/";
	/** {@value} */
	public static final String PACKAGE_SEPARATOR = ".";
	/** {@value} */
	public static final String SUBCLASS_SEPARATOR = "$";
	/** {@value} */
	public static final String CLASS_METHOD_SEPARATOR = ".";
	/** {@value} */
	public static final String ARGUMENTS_SEPARATOR = ",";
	/** {@value} */
	public static final String ARGUMENTS_BEGIN = "(";
	/** {@value} */
	public static final String ARGUMENTS_END = ")";
	/** {@value} */
	public static final String RETURN_TYPE_SEPARATOR = ":";
	/** {@value} */
	public static final String ARRAY_BRACKETS = "[]";

	/** Class names of wrapper types. */
	public static final Set<String> WRAPPER_TYPE_CLASS_NAMES = new HashSet<>();

	/** Static initializer. */
	static {
		WRAPPER_TYPE_CLASS_NAMES.add(Boolean.class.getName());
		WRAPPER_TYPE_CLASS_NAMES.add(Byte.class.getName());
		WRAPPER_TYPE_CLASS_NAMES.add(Short.class.getName());
		WRAPPER_TYPE_CLASS_NAMES.add(Integer.class.getName());
		WRAPPER_TYPE_CLASS_NAMES.add(Character.class.getName());
		WRAPPER_TYPE_CLASS_NAMES.add(Long.class.getName());
		WRAPPER_TYPE_CLASS_NAMES.add(Float.class.getName());
		WRAPPER_TYPE_CLASS_NAMES.add(Double.class.getName());
	}
}
