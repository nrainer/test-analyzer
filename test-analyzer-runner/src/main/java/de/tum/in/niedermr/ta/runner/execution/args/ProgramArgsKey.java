package de.tum.in.niedermr.ta.runner.execution.args;

public final class ProgramArgsKey {
	private final Class<?> m_programClass;
	private final int m_index;

	public ProgramArgsKey(Class<?> programClass, int index) {
		m_programClass = programClass;
		m_index = index;
	}

	public Class<?> getProgramClass() {
		return m_programClass;
	}

	public int getIndex() {
		return m_index;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProgramArgsKey) {
			ProgramArgsKey other = (ProgramArgsKey) obj;

			return m_programClass == other.m_programClass && m_index == other.m_index;
		}

		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return m_programClass.hashCode() * 7 + m_index;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "ProgramArgsKey [m_programClass=" + m_programClass + ", m_index=" + m_index + "]";
	}
}
