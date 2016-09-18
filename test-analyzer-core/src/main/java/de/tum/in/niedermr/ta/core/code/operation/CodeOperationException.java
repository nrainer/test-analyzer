package de.tum.in.niedermr.ta.core.code.operation;

/** Code operation exception. */
public class CodeOperationException extends Exception {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Constructor. */
	public CodeOperationException(String message, Throwable t) {
		super(message, t);
	}
}
