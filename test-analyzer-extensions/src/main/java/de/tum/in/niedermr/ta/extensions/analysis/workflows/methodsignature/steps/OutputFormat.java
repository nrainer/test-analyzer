package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps;

/** Output format for {@link ReturnTypeCollectorStep}. */
public enum OutputFormat {

	/** List of classes. */
	LIST,
	/** List of classes with occurrence information. */
	COUNT,
	/** Switch-case code. */
	CODE;

	/** Get the default output format. */
	public static OutputFormat getDefault() {
		return OutputFormat.LIST;
	}
}
