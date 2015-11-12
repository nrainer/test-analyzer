package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class ReturnValueGeneratorUtil {

	private ReturnValueGeneratorUtil() {
	}

	public static boolean canHandleType(IReturnValueGenerator returnValueGenerator, String className, String methodName, String desc) {
		return canHandleType(returnValueGenerator, MethodIdentifier.create(className, methodName, desc), desc);
	}

	public static boolean canHandleType(IReturnValueGenerator returnValueGenerator, MethodIdentifier methodIdentifier, String desc) {
		return returnValueGenerator.checkReturnValueSupported(methodIdentifier, Type.getReturnType(desc));
	}
}
