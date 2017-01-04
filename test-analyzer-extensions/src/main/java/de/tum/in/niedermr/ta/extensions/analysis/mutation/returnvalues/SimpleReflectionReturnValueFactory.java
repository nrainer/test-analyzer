package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueFactory;
import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/**
 * Uses reflection to create instances of
 * <li>classes with public parameterless constructors</li>
 * <li>classes with a public constructor with parameters of primitive types or String</li>
 * <li>enums</li>
 * <li>arrays (one dimensional only)</li>
 */
public class SimpleReflectionReturnValueFactory extends AbstractReturnValueFactory {

	/** Instance. */
	public static final SimpleReflectionReturnValueFactory INSTANCE = new SimpleReflectionReturnValueFactory();

	/**
	 * {@link #createInstanceWithSimpleParameters(Constructor)} supports primitive types, arrays and this set of classes
	 * as parameter types.
	 */
	private static final Set<Class<?>> SUPPORTED_CONSTRUCTOR_PARAMETER_TYPES = new HashSet<>(
			Arrays.asList(Object.class, String.class, Optional.class));

	/** {@inheritDoc} */
	@Override
	public Object createWithException(MethodIdentifier methodIdentifier, String returnType) throws NoSuchElementException {
		try {
			if (isBlacklistedType(returnType)) {
				throw new NoSuchElementException("Blacklisted: " + returnType);
			}

			Class<?> cls = resolveClass(returnType);

			if (returnType.endsWith(JavaConstants.ARRAY_BRACKETS)) {
				return createArray(returnType, cls);
			}

			if (cls.isEnum()) {
				return getEnumInstance(cls);
			}

			if (cls.isInterface()) {
				throw new NoSuchElementException("Interfaces are not supported: " + cls);
			}

			return createInstance(cls);
		} catch (ReflectiveOperationException e) {
			throw new NoSuchElementException();
		}
	}

	protected Class<?> resolveClass(String returnType) throws ClassNotFoundException {
		String cleanedClassName = returnType.replace(JavaConstants.ARRAY_BRACKETS, "");
		return Class.forName(cleanedClassName);
	}

	protected Object createInstance(Class<?> cls) throws ReflectiveOperationException {
		Constructor<?>[] publicConstructors = cls.getConstructors();

		for (Constructor<?> constructor : publicConstructors) {
			if (constructor.getParameterTypes().length == 0) {
				return constructor.newInstance();
			} else if (areAllPrimitiveTypeOrStringParameters(constructor)) {
				return createInstanceWithSimpleParameters(constructor);
			}
		}

		throw new NoSuchElementException("No supported constructor exists: " + cls);
	}

	protected static Object createInstanceWithSimpleParameters(Constructor<?> constructor)
			throws ReflectiveOperationException {
		Class<?>[] parameterTypes = constructor.getParameterTypes();
		Object[] parameterValues = new Object[parameterTypes.length];

		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> parameterType = parameterTypes[i];
			Object parameterValue;

			if (parameterType.isArray()) {
				parameterValue = Array.newInstance(parameterType.getComponentType(), 0);
			} else if (parameterType == String.class) {
				parameterValue = "";
			} else if (parameterType == Object.class) {
				parameterValue = new Object();
			} else if (parameterType == Optional.class) {
				parameterValue = Optional.empty();
			} else if (parameterType == boolean.class) {
				parameterValue = true;
			} else if (parameterType == char.class) {
				parameterValue = ' ';
			} else if (parameterType == byte.class) {
				parameterValue = (byte) 1;
			} else if (parameterType == short.class) {
				parameterValue = 1;
			} else if (parameterType == int.class) {
				parameterValue = 1;
			} else if (parameterType == long.class) {
				parameterValue = 1L;
			} else if (parameterType == float.class) {
				parameterValue = (float) 1.0;
			} else if (parameterType == double.class) {
				parameterValue = 1.0;
			} else {
				throw new IllegalStateException("Not a primitive type or String: " + parameterType);
			}

			parameterValues[i] = parameterValue;
		}

		return constructor.newInstance(parameterValues);
	}

	private boolean areAllPrimitiveTypeOrStringParameters(Constructor<?> constructor) {
		for (Class<?> parameterType : constructor.getParameterTypes()) {
			if (!parameterType.isPrimitive() && !parameterType.isArray()
					&& !SUPPORTED_CONSTRUCTOR_PARAMETER_TYPES.contains(parameterType)) {
				return false;
			}
		}

		return true;
	}

	private Object getEnumInstance(Class<?> enumCls) {
		Object[] enumConstants = enumCls.getEnumConstants();

		if (enumConstants.length > 0) {
			return enumConstants[0];
		}

		throw new NoSuchElementException("Enum does not contain any values: " + enumCls);
	}

	private Object createArray(String returnType, Class<?> arrayCls) {
		if (returnType.endsWith(JavaConstants.ARRAY_BRACKETS + JavaConstants.ARRAY_BRACKETS)) {
			throw new NoSuchElementException("Only arrays of a single dimension supported: " + returnType);
		}

		return Array.newInstance(arrayCls, 0);
	}

	/**
	 * Is blacklisted type.
	 * 
	 * @param className
	 *            may contain array brackets
	 */
	protected boolean isBlacklistedType(String className) {
		if (String.class.getName().equals(className) || JavaConstants.WRAPPER_TYPE_CLASS_NAMES.contains(className)) {
			// already handled by the simple return value generator classes
			return true;
		}

		return false;
	}
}
