package de.tum.in.niedermr.ta.core.analysis.filter.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class MethodNameFilter implements IMethodFilter {
	private Set<MethodIdentifier> m_methodIdentifierSet;

	public MethodNameFilter(MethodIdentifier... methodIdentifiers) {
		this.m_methodIdentifierSet = new HashSet<>(Arrays.asList(methodIdentifiers));
	}

	@Override
	public FilterResult apply(MethodIdentifier methodIdentifier, MethodNode method) {
		return FilterResult.create(m_methodIdentifierSet.contains(methodIdentifier), MethodNameFilter.class);
	}
}
