package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueFactory;
import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/**
 * Uses reflection to create instances of
 * <li>classes with public parameterless constructors</li>
 * <li>enums</li>
 * <li>arrays (one dimensional only)</li>
 */
public class SimpleReflectionReturnValueFactory extends AbstractReturnValueFactory {

	/** Instance. */
	public static final SimpleReflectionReturnValueFactory INSTANCE = new SimpleReflectionReturnValueFactory();

	/** {@inheritDoc} */
	@Override
	public Object getWithException(MethodIdentifier methodIdentifier, String returnType) throws NoSuchElementException {
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

	protected Object createInstance(Class<?> cls)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		Constructor<?>[] publicConstructors = cls.getConstructors();

		for (Constructor<?> constructor : publicConstructors) {
			if (constructor.getParameterTypes().length == 0) {
				return constructor.newInstance();
			}
		}

		throw new NoSuchElementException("No public parameterless constructor exists: " + cls);
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
		if (String.class.getName().equals(className)) {
			// already handled by the simple return value generator classes
			return true;
		}

		return false;
	}
}
