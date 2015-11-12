package de.tum.in.niedermr.ta.core.analysis.filter.advanced;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class SimpleCodeComplexityFilter implements IMethodFilter {

	private final int m_complexityThreshold;

	public SimpleCodeComplexityFilter() {
		this(10);
	}

	public SimpleCodeComplexityFilter(int complexityThreshold) {
		m_complexityThreshold = complexityThreshold;
	}

	@Override
	public FilterResult acceptMethod(MethodIdentifier methodIdentifier, MethodNode method) {
		boolean filterResult = isMethodMoreComplexThanThreshold(method.maxLocals, method.maxStack);
		return FilterResult.create(filterResult, SimpleCodeComplexityFilter.class);
	}

	protected boolean isMethodMoreComplexThanThreshold(int maxLocals, int maxStack) {
		return (maxLocals + maxStack) > m_complexityThreshold;
	}
}
