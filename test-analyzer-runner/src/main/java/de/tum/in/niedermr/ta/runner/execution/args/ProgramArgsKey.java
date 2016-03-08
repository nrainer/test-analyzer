package de.tum.in.niedermr.ta.runner.execution.args;

/** Key to access a program argument. */
public final class ProgramArgsKey {
	/** Main class */
	private final Class<?> m_programClass;
	/** Index in the args array. */
	private final int m_index;

	/** Constructor. */
	public ProgramArgsKey(Class<?> programClass, int index) {
		m_programClass = programClass;
		m_index = index;
	}

	/** Check if this key is suitable for the given program class. */
	public boolean isForProgramClass(Class<?> programClass) {
		return m_programClass == programClass;
	}

	/** @see #m_index */
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
