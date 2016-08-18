package de.tum.in.niedermr.ta.core.analysis.filter.advanced;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Filters hashCode methods out. */
public class HashCodeMethodFilter implements IMethodFilter {
	private static final String HASH_CODE_METHOD_NAME = "hashCode";

	/** {@inheritDoc} */
	@Override
	public FilterResult acceptMethod(MethodIdentifier identifier, MethodNode method) {
		boolean isHashcodeMethod = identifier.getOnlyMethodName().equals(HASH_CODE_METHOD_NAME);
		return FilterResult.create(!isHashcodeMethod, HashCodeMethodFilter.class);
	}
}
