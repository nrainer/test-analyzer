package de.tum.in.niedermr.ta.core.analysis.filter.advanced;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class HashCodeMethodFilter implements IMethodFilter {
	private static final String HASH_CODE_METHOD_NAME = "hashCode";

	@Override
	public FilterResult acceptMethod(MethodIdentifier identifier, MethodNode method) {
		return FilterResult.create(!(identifier.getOnlyMethodName().equals(HASH_CODE_METHOD_NAME)), HashCodeMethodFilter.class);
	}
}
