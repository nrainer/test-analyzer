package de.tum.in.niedermr.ta.runner.execution.id;

import java.util.Objects;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;

/** Implementation of {@link IExecutionId} and {@link IFullExecutionId}. */
final class ExecutionId implements IExecutionId, IFullExecutionId {

	/** Separator between the short execution id and the suffix (if present). */
	static final String SEPARATOR = "_";

	/** Short execution id. */
	private final String m_shortExecutionId;
	/** Suffix of long execution id. May be null. */
	private final String m_fullExecutionSuffix;

	/** Constructor. */
	ExecutionId(String shortExecutionId) {
		this(shortExecutionId, null);
	}

	/** Constructor. */
	ExecutionId(String shortExecutionId, String suffix) {
		m_shortExecutionId = Objects.requireNonNull(shortExecutionId);
		m_fullExecutionSuffix = suffix;

		if (isFullExecutionId(shortExecutionId)) {
			throw new IllegalArgumentException("Execution id must not contain the separator: " + shortExecutionId);
		}
	}

	/** Parse a short execution id. */
	static IExecutionId parseShortExecutionId(String executionId) {
		return parseShortExecutionId(executionId, false);
	}

	/** Parse a short execution id. */
	static IExecutionId parseShortExecutionId(String executionId, boolean allowTrimming) {
		if (ExecutionId.isFullExecutionId(executionId)) {
			if (!allowTrimming) {
				throw new IllegalArgumentException("Expected a short execution id: " + executionId);
			}

			return parseFullExecutionId(executionId).convertToShortExecutionId();
		}
		return new ExecutionId(executionId);
	}

	/**
	 * Parse a full execution id.
	 * 
	 * @throws IllegalArgumentException
	 *             if a short execution id is provided
	 */
	static IFullExecutionId parseFullExecutionId(String value) {
		if (isFullExecutionId(value)) {
			String executionId = value.substring(0, value.indexOf(SEPARATOR));
			String suffix = value.substring(value.indexOf(SEPARATOR) + 1);
			return new ExecutionId(executionId, suffix);
		}

		throw new IllegalArgumentException("Not a full execution id: " + value);
	}

	/** Check if the value can be parse as full execution id. */
	static boolean isFullExecutionId(String value) {
		return value.contains(SEPARATOR);
	}

	/** {@inheritDoc} */
	@Override
	public String getShortId() {
		return m_shortExecutionId;
	}

	/** {@inheritDoc} */
	@Override
	public String getFullId() {
		return m_shortExecutionId + SEPARATOR + m_fullExecutionSuffix;
	}

	/** {@inheritDoc} */
	@Override
	public IFullExecutionId createFullExecutionId(String suffix) {
		return new ExecutionId(m_shortExecutionId, suffix);
	}

	/** {@inheritDoc} */
	@Override
	public IExecutionId convertToShortExecutionId() {
		return new ExecutionId(m_shortExecutionId);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return get();
	}

	/** {@inheritDoc} */
	@Override
	public String get() {
		if (m_fullExecutionSuffix != null) {
			return getFullId();
		}
		return getShortId();
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return Objects.hash(m_shortExecutionId, m_fullExecutionSuffix);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object other) {
		if (other instanceof ExecutionId) {
			return Objects.equals(this.m_shortExecutionId, ((ExecutionId) other).m_shortExecutionId)
					&& Objects.equals(this.m_fullExecutionSuffix, ((ExecutionId) other).m_fullExecutionSuffix);
		}

		return false;
	}
}
