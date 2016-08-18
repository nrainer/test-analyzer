package de.tum.in.niedermr.ta.core.analysis.filter.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.OpcodesUtility;

/** Filter synthetic Java methods. */
public class SyntheticMethodFilter implements IMethodFilter {

	/** {@inheritDoc} */
	@Override
	public FilterResult apply(MethodIdentifier methodIdentifier, MethodNode methodNode) throws Exception {
		if (OpcodesUtility.hasFlag(methodNode.access, Opcodes.ACC_SYNTHETIC)) {
			return FilterResult.reject(getClass());
		}

		return FilterResult.accepted();
	}
}
