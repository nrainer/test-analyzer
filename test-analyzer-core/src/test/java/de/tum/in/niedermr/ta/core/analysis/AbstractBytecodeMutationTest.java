package de.tum.in.niedermr.ta.core.analysis;

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
}
