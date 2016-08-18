package de.tum.in.niedermr.ta.core.analysis.filter.core;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.ReturnValueGeneratorUtil;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class ValueGenerationSupportedFilter implements IMethodFilter {
	private final IReturnValueGenerator m_returnValueGenerator;

	public ValueGenerationSupportedFilter(IReturnValueGenerator returnValueGenerator) {
		this.m_returnValueGenerator = returnValueGenerator;
	}

	@Override
	public FilterResult acceptMethod(MethodIdentifier methodIdentifier, MethodNode method) {
		if (ReturnValueGeneratorUtil.canHandleType(m_returnValueGenerator, methodIdentifier, method.desc)) {
			return FilterResult.accepted();
		} else {
			return FilterResult.reject(ValueGenerationSupportedFilter.class, "Return type not supported by " + m_returnValueGenerator.getClass().getName());
		}
	}
}
