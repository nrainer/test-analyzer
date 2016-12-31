package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Factory to create return value instances. */
public interface IReturnValueFactory {

	boolean supports(MethodIdentifier methodIdentifier, String returnType);

	Object get(String identifierAsString, String returnType);
}