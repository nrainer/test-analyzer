package de.tum.in.niedermr.ta.core.code.identifier;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.util.Identification;

public final class MethodIdentifier implements Identifier, JavaConstants {
	private static final String UNKNOWN_RETURN_TYPE = "?";
	public static final MethodIdentifier EMPTY = new MethodIdentifier("*", UNKNOWN_RETURN_TYPE);

	private final String m_identifier;
	private final String m_returnType;

	private MethodIdentifier(String identifier, String returnType) {
		this.m_identifier = identifier;
		this.m_returnType = returnType;
	}

	public static MethodIdentifier create(Class<?> cls, MethodNode method) {
		return create(cls.getName(), method);
	}

	/**
	 * @param className
	 *            either with dots or slashes
	 */
	public static MethodIdentifier create(String className, MethodNode method) {
		return create(className, method.name, method.desc);
	}

	public static MethodIdentifier create(Class<?> cls, String methodName, String methodDesc) {
		return MethodIdentifier.create(cls.getName(), methodName, methodDesc);
	}

	/**
	 * @param className
	 *            either with dots or slashes
	 */
	public static MethodIdentifier create(String className, String methodName, String methodDesc) {
		String identifier = Identification.asClassName(className) + CLASS_METHOD_SEPARATOR + methodName + convertDescriptor(methodDesc);
		String returnType = Identification.getMethodReturnType(methodDesc);

		return new MethodIdentifier(identifier, returnType);
	}

	public static MethodIdentifier parse(String methodIdentifier) {
		String[] values = methodIdentifier.split(RETURN_TYPE_SEPARATOR);

		return new MethodIdentifier(values[0], values.length > 1 ? values[1] : UNKNOWN_RETURN_TYPE);
	}

	@Override
	public final String get() {
		return m_identifier;
	}

	/**
	 * @see #getOnlyReturnType()
	 */
	public final String getWithReturnType() {
		return get() + RETURN_TYPE_SEPARATOR + getOnlyReturnType();
	}

	/**
	 * The return type would be available if the instance was created using the 'create' method or if the string to be parsed by the 'parse' method contained
	 * the return type. Otherwise {@link #UNKNOWN_RETURN_TYPE} will be returned.
	 */
	public final String getOnlyReturnType() {
		return m_returnType;
	}

	public final String getOnlyClassName() {
		String s = get();

		try {
			// cut the arguments
			s = s.substring(0, s.indexOf(ARGUMENTS_BEGIN));

			// cut the method name
			s = s.substring(0, s.lastIndexOf(CLASS_METHOD_SEPARATOR));
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalStateException("Method identifier is invalid: " + get());
		}

		return s;
	}

	public final String getOnlyMethodName() {
		String s = get();

		try {
			// cut the arguments
			s = s.substring(0, s.indexOf(ARGUMENTS_BEGIN));

			// cut the method name
			s = s.substring(s.lastIndexOf(CLASS_METHOD_SEPARATOR) + 1);
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalStateException("Method identifier is invalid: " + get());
		}

		return s;
	}

	@Override
	public int hashCode() {
		return m_identifier.hashCode();
	}

	/**
	 * Two method identifiers are supposed to be equal if the identifiers are equal. The return type is not considered.
	 */
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof MethodIdentifier && this.m_identifier.equals(((MethodIdentifier) obj).m_identifier);
	}

	private static String convertDescriptor(String descriptor) {
		Type type = Type.getType(descriptor);

		StringBuilder builder = new StringBuilder();

		for (Type argument : type.getArgumentTypes()) {
			builder.append(ARGUMENTS_SEPARATOR);
			builder.append(argument.getClassName());
		}

		String arguments = builder.toString();

		if (!arguments.isEmpty()) {
			arguments = arguments.substring(1);
		}

		return ARGUMENTS_BEGIN + arguments + ARGUMENTS_END;
	}

	@Override
	public String toString() {
		return get();
	}
}
