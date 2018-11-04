package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import de.tum.in.niedermr.ta.core.analysis.AbstractBytecodeMutationTest;
import de.tum.in.niedermr.ta.core.analysis.filter.MethodFilterList;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.visitor.BytecodeModificationTestUtility;
import de.tum.in.niedermr.ta.runner.analysis.mutation.MutateMethodsOperation;

/** Abstract test class for {@link IReturnValueGenerator}. */
public abstract class AbstractReturnValueGeneratorTest<T> extends AbstractBytecodeMutationTest<T> {

	private final IReturnValueGenerator m_returnValueGenerator;

	/** Constructor. */
	public AbstractReturnValueGeneratorTest(Class<T> classToBeMutated, IReturnValueGenerator returnValueGenerator) {
		super(classToBeMutated);
		m_returnValueGenerator = returnValueGenerator;
	}

	/** {@inheritDoc} */
	@Override
	protected Class<?> modifyClass(Class<?> classToBeMutated) throws Exception {
		MethodFilterList filterList = MethodFilterList.createWithDefaultFilters();
		filterList.addValueGenerationSupportedFilter(m_returnValueGenerator);
		ICodeModificationOperation modificationOperation = new MutateMethodsOperation(m_returnValueGenerator,
				filterList);
		return BytecodeModificationTestUtility.modifyAndLoadClass(classToBeMutated, modificationOperation);
	}
}
