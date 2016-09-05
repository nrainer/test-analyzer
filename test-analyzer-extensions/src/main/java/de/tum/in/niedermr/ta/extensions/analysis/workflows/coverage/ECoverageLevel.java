package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

/** Coverage level. */
public enum ECoverageLevel {
	/** Method level. */
	METHOD("cov_method"),
	/** Line level. */
	LINE("cov_line"),
	/** Instruction level. */
	INSTRUCTION("cov_instruction"),
	/** Branch level. */
	BRANCH("cov_branch");

	private final String m_valueName;

	/** Constructor. */
	private ECoverageLevel(String valueName) {
		m_valueName = valueName;
	}

	public String getValueName() {
		return m_valueName;
	}
}
