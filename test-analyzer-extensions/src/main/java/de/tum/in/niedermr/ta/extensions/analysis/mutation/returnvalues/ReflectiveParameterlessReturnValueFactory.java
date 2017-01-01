package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class ReflectiveParameterlessReturnValueFactory extends AbstractReturnValueFactory {
	public static final ReflectiveParameterlessReturnValueFactory INSTANCE = new ReflectiveParameterlessReturnValueFactory();

	/** {@inheritDoc} */
	@Override
	public Object getWithException(MethodIdentifier methodIdentifier, String returnType) throws NoSuchElementException {
		try {
			Class<?> cls = Class.forName(returnType);

			if (isBlacklistedClass(cls)) {
				throw new NoSuchElementException();
			}

			Constructor<?>[] publicConstructors = cls.getConstructors();

			for (Constructor<?> constructor : publicConstructors) {
				if (constructor.getParameterTypes().length == 0) {
					return constructor.newInstance();
				}
			}

		} catch (ReflectiveOperationException e) {
			// NOP
		}

		throw new NoSuchElementException();
	}

	protected boolean isBlacklistedClass(Class<?> cls) {
		if (cls == String.class) {
			// already handled by the simple return value generator classes
			return true;
		}

		return false;
	}
}
