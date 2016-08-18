package de.tum.in.niedermr.ta.core.analysis.filter;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public interface IMethodFilter {

	/** Apply the filters to the method. */
	FilterResult apply(MethodIdentifier methodIdentifier, MethodNode methodNode) throws Exception;
}
