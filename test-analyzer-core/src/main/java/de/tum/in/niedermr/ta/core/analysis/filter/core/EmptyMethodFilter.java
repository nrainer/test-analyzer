package de.tum.in.niedermr.ta.core.analysis.filter.core;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;

public class EmptyMethodFilter implements IMethodFilter {

	/** {@inheritDoc} */
	@Override
	public FilterResult apply(MethodIdentifier methodIdentifier, MethodNode methodNode) {
		int countInstructions = BytecodeUtility.countMethodInstructions(methodNode);

		if (countInstructions == 1) {
			// single return opcode
			return FilterResult.reject(EmptyMethodFilter.class);
		}

		return FilterResult.accepted();
	}
}
