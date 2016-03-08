package de.tum.in.niedermr.ta.runner.execution.args;

import java.util.Objects;

public abstract class AbstractProgramArgsManager {

	private final Class<?> m_programClass;
	protected final String[] m_args;

	public AbstractProgramArgsManager(Class<?> programClass, String[] args) {
		m_programClass = Objects.requireNonNull(programClass);
		m_args = Objects.requireNonNull(args);
	}

	protected void checkProgramArgsKey(ProgramArgsKey key) {
		if (m_programClass != key.getProgramClass()) {
			throw new IllegalArgumentException("Key is not suitable for " + m_programClass.getName());
		}

		if (key.getIndex() >= m_args.length) {
			throw new IllegalArgumentException("Index is out of range: " + key.getIndex());
		}
	}
}
