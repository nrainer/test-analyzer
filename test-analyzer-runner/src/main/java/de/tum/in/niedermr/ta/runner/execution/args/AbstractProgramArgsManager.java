package de.tum.in.niedermr.ta.runner.execution.args;

import java.util.Objects;

/** Abstract class for handling program arguments. */
public abstract class AbstractProgramArgsManager {

	private final Class<?> m_programClass;
	protected final String[] m_args;

	/** Constructor. */
	public AbstractProgramArgsManager(Class<?> programClass, String[] args) {
		m_programClass = Objects.requireNonNull(programClass);
		m_args = Objects.requireNonNull(args);
	}

	/** Check if a key is suitable for the program and if its index is in the args range. */
	protected void checkProgramArgsKey(ProgramArgsKey key) {
		if (key.isForProgramClass(m_programClass)) {
			throw new IllegalArgumentException("Key is not suitable for " + m_programClass.getName());
		}

		if (key.getIndex() >= m_args.length) {
			throw new IllegalArgumentException("Index is out of range: " + key.getIndex());
		}
	}
}
