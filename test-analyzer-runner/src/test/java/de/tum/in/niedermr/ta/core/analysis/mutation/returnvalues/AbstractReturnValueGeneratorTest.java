package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import java.io.IOException;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.filter.MethodFilterList;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.visitor.BytecodeModificationTestUtility;
import de.tum.in.niedermr.ta.runner.analysis.mutation.MutateMethodsOperation;

/** Abstract test class for {@link IReturnValueGenerator}. */
public abstract class AbstractReturnValueGeneratorTest<T> {

	private final IReturnValueGenerator m_returnValueGenerator;
	private final Class<T> m_classToBeMutated;

	/** Constructor. */
	public AbstractReturnValueGeneratorTest(IReturnValueGenerator returnValueGenerator, Class<T> classToBeMutated) {
		m_returnValueGenerator = returnValueGenerator;
		m_classToBeMutated = classToBeMutated;
	}

	/** Test the mutation. */
	@Test
	public void testMutation() throws ReflectiveOperationException, CodeOperationException, IOException {
		MethodFilterList filterList = MethodFilterList.createWithDefaultFilters();
		filterList.addValueGenerationSupportedFilter(m_returnValueGenerator);

		Class<?> mutatedClass = mutateClass(m_classToBeMutated, filterList);
		Object instanceOfMutatedClass = mutatedClass.newInstance();
		T instanceOfOriginalClass = m_classToBeMutated.newInstance();
		verifyMutation(mutatedClass, instanceOfMutatedClass, instanceOfOriginalClass);
	}

	/** Mutate the class. */
	protected Class<?> mutateClass(Class<?> classToBeMutated, MethodFilterList filterList)
			throws ClassNotFoundException, CodeOperationException, IOException {
		ICodeModificationOperation modificationOperation = new MutateMethodsOperation(m_returnValueGenerator,
				filterList);
		return BytecodeModificationTestUtility.createAndLoadModifiedClass(classToBeMutated, modificationOperation);
	}

	/** Test the mutation. */
	protected abstract void verifyMutation(Class<?> mutatedClass, Object instanceOfMutatedClass,
			T instanceOfOriginalClass) throws ReflectiveOperationException;
}
