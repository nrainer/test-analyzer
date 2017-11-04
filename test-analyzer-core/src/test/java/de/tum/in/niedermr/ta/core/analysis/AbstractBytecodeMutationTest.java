package de.tum.in.niedermr.ta.core.analysis;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/** Abstract test class for bytecode modifications. */
public abstract class AbstractBytecodeMutationTest<T> {

	private Class<T> m_classToBeMutated;

	/** Constructor. */
	public AbstractBytecodeMutationTest(Class<T> classToBeMutated) {
		m_classToBeMutated = classToBeMutated;
	}

	/** Test. */
	@Test
	public void testBytecodeModification() throws Exception {
		Class<?> mutatedClass = modifyClass(m_classToBeMutated);
		Object instanceOfMutatedClass = mutatedClass.newInstance();
		T instanceOfOriginalClass = m_classToBeMutated.newInstance();
		verifyModification(mutatedClass, instanceOfMutatedClass, instanceOfOriginalClass);
	}

	/** Modify the class. */
	protected abstract Class<?> modifyClass(Class<?> classToBeModified) throws Exception;

	/** Verify the modified class. */
	protected abstract void verifyModification(Class<?> mutatedClass, Object instanceOfMutatedClass,
			T instanceOfOriginalClass) throws Exception;

	/** Invoke a method. */
	protected final void invokeMethod(Object instanceOfModifiedClass, String methodName, Object... params)
			throws ReflectiveOperationException {

		Class<?>[] paramTypes = new Class<?>[params.length];

		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getClass();
		}

		instanceOfModifiedClass.getClass().getMethod(methodName, paramTypes).invoke(instanceOfModifiedClass, params);
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
