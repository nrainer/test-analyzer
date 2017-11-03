package de.tum.in.niedermr.ta.core.analysis;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/** Abstract test class for bytecode modifications. */
public abstract class AbstractBytecodeMutationTest<T> {

	private Class<T> m_classToBeMutated;

	public AbstractBytecodeMutationTest(Class<T> classToBeMutated) {
		m_classToBeMutated = classToBeMutated;
	}

	/** Test. */
	@Test
	public void testBytecodeModification() throws Exception {
		Class<?> mutatedClass = mutateClass(m_classToBeMutated);
		Object instanceOfMutatedClass = mutatedClass.newInstance();
		T instanceOfOriginalClass = m_classToBeMutated.newInstance();
		verifyMutation(mutatedClass, instanceOfMutatedClass, instanceOfOriginalClass);
	}

	/** Modify the class. */
	protected abstract Class<?> mutateClass(Class<?> classToBeMutated) throws Exception;

	/** Test the modified class. */
	protected abstract void verifyMutation(Class<?> mutatedClass, Object instanceOfMutatedClass,
			T instanceOfOriginalClass) throws Exception;

	/** Invoke a method. */
	protected final void invokeMethod(Object instanceOfMutatedClass, String methodName, Object... params)
			throws ReflectiveOperationException {

		Class<?>[] paramTypes = new Class<?>[params.length];

		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getClass();
		}

		instanceOfMutatedClass.getClass().getMethod(methodName, paramTypes).invoke(instanceOfMutatedClass, params);
	}

	/** Invoke a method. */
	protected final void invokeMethodNoInvocationEx(Object instanceOfMutatedClass, String methodName, Object... params)
			throws ReflectiveOperationException {
		try {
			invokeMethod(instanceOfMutatedClass, methodName, params);
		} catch (InvocationTargetException e) {
			// NOP
		}
	}
}
