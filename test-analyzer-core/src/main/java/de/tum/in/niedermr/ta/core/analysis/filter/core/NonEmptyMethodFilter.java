package de.tum.in.niedermr.ta.core.analysis.filter.core;

import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;

public class NonEmptyMethodFilter implements IMethodFilter {

	@Override
	public FilterResult acceptMethod(MethodIdentifier methodIdentifier, MethodNode methodNode) throws Exception {
		int countInstructions = BytecodeUtility.countMethodInstructions(methodNode);

		if (countInstructions == 1) {
			return FilterResult.reject(NonEmptyMethodFilter.class);
		} else {
			return FilterResult.accepted();
		}
	}
}
