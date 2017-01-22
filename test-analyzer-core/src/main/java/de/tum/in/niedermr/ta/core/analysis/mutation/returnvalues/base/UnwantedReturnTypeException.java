package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

/**
 * Exception to indicate that a factory does not want to create an instance of a given type and therefore, the fallback
 * factory should not be used to create an instance of that type either.
 */
public class UnwantedReturnTypeException extends Exception {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Constructor. */
	public UnwantedReturnTypeException() {
		// NOP
	}

	/** Constructor. */
	public UnwantedReturnTypeException(String message) {
		super(message);
	}
}
