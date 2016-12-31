package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Return value generator utils. */
public class ReturnValueGeneratorUtil {

	/** Constructor. */
	private ReturnValueGeneratorUtil() {
	}

	public static boolean canHandleType(IReturnValueGenerator returnValueGenerator, MethodIdentifier methodIdentifier,
			String desc) {
		return returnValueGenerator.checkReturnValueSupported(methodIdentifier, Type.getReturnType(desc));
	}
}
