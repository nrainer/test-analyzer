package de.tum.in.niedermr.ta.core.code.identifier;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.util.Identification;

/** Identifier for Java methods. */
public final class MethodIdentifier implements Identifier {
	private static final String UNKNOWN_RETURN_TYPE = "?";
	public static final MethodIdentifier EMPTY = new MethodIdentifier("*", UNKNOWN_RETURN_TYPE);

	private final String m_identifier;
	private final String m_returnType;

	/** Constructor. */
	private MethodIdentifier(String identifier, String returnType) {
		this.m_identifier = identifier;
		this.m_returnType = returnType;
	}

	/**
	 * Create an identifier.
	 * 
	 * @param cls
	 *            class instance
	 * @param methodNode
	 *            ASM method node
	 */
	public static MethodIdentifier create(Class<?> cls, MethodNode methodNode) {
		return create(cls.getName(), methodNode);
	}

	/**
	 * Create an identifier.
	 * 
	 * @param classNode
	 *            ASM class node
	 * @param methodNode
	 *            ASM method node
	 */
	public static MethodIdentifier create(ClassNode classNode, MethodNode methodNode) {
		return create(classNode.name, methodNode);
	}

	/**
	 * Create an identifier.
	 * 
	 * @param className
	 *            class name (dot separator) or class path (slash separator)
	 * @param methodNode
	 *            ASM method node
	 */
	public static MethodIdentifier create(String className, MethodNode methodNode) {
		return create(className, methodNode.name, methodNode.desc);
	}

	/**
	 * Create an identifier.
	 * 
	 * @param cls
	 *            class instance
	 * @param methodName
	 *            name of the method
	 * @param methodDesc
	 *            descriptor of the method
	 */
	public static MethodIdentifier create(Class<?> cls, String methodName, String methodDesc) {
		return create(cls.getName(), methodName, methodDesc);
	}

	/**
	 * @param className
	 *            either with dots or slashes
	 */
	public static MethodIdentifier create(String className, String methodName, String methodDesc) {
		String identifier = Identification.asClassName(className) + JavaConstants.CLASS_METHOD_SEPARATOR + methodName
				+ convertDescriptor(methodDesc);
		String returnType = Identification.getMethodReturnType(methodDesc);

		return new MethodIdentifier(identifier, returnType);
	}

	public static MethodIdentifier parse(String methodIdentifier) {
		String[] values = methodIdentifier.split(JavaConstants.RETURN_TYPE_SEPARATOR);
		String returnType = UNKNOWN_RETURN_TYPE;

		if (values.length > 1) {
			returnType = values[1];
		}

		return new MethodIdentifier(values[0], returnType);
	}

	/** {@inheritDoc} */
	@Override
	public final String get() {
		return m_identifier;
	}

	/**
	 * @see #getOnlyReturnType()
	 */
	public final String getWithReturnType() {
		return get() + JavaConstants.RETURN_TYPE_SEPARATOR + getOnlyReturnType();
	}

	/**
	 * The return type would be available if the instance was created using the
	 * 'create' method or if the string to be parsed by the 'parse' method contained
	 * the return type. Otherwise {@link #UNKNOWN_RETURN_TYPE} will be returned.
	 */
	public final String getOnlyReturnType() {
		return m_returnType;
	}

	public final String getOnlyClassName() {
		String s = get();

		try {
			// cut the arguments
			s = s.substring(0, s.indexOf(JavaConstants.ARGUMENTS_BEGIN));

			// cut the method name
			s = s.substring(0, s.lastIndexOf(JavaConstants.CLASS_METHOD_SEPARATOR));
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalStateException("Method identifier is invalid: " + get());
		}

		return s;
	}

	public final String getOnlyMethodName() {
		String s = get();

		try {
			// cut the arguments
			s = s.substring(0, s.indexOf(JavaConstants.ARGUMENTS_BEGIN));

			// cut the method name
			s = s.substring(s.lastIndexOf(JavaConstants.CLASS_METHOD_SEPARATOR) + 1);
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalStateException("Method identifier is invalid: " + get());
		}

		return s;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return m_identifier.hashCode();
	}

	/**
	 * Two method identifiers are supposed to be equal if the identifiers are equal.
	 * The return type is not considered.
	 */
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof MethodIdentifier
				&& this.m_identifier.equals(((MethodIdentifier) obj).m_identifier);
	}

	private static String convertDescriptor(String descriptor) {
		Type type = Type.getType(descriptor);

		StringBuilder builder = new StringBuilder();

		for (Type argument : type.getArgumentTypes()) {
			builder.append(JavaConstants.ARGUMENTS_SEPARATOR);
			builder.append(argument.getClassName());
		}

		String arguments = builder.toString();

		if (!arguments.isEmpty()) {
			arguments = arguments.substring(1);
		}

		return JavaConstants.ARGUMENTS_BEGIN + arguments + JavaConstants.ARGUMENTS_END;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return get();
	}
}
