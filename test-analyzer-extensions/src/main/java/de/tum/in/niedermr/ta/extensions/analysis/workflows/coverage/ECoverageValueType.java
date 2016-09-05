package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

/** Coverage value type. */
public enum ECoverageValueType {
	/** Number of covered units. */
	COVERED_COUNT("_covered"),
	/** Number of all units. */
	ALL_COUNT("_all");

	private final String m_postFix;

	/** Constructor. */
	private ECoverageValueType(String postFix) {
		m_postFix = postFix;
	}

	public String getPostFix() {
		return m_postFix;
	}
}
