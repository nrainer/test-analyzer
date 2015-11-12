package de.tum.in.niedermr.ta.core.analysis.filter;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public interface IMethodFilter {

	/**
	 * Check if the given method shall be processed or skipped.
	 */
	FilterResult acceptMethod(MethodIdentifier methodIdentifier, MethodNode methodNode) throws Exception;
}
