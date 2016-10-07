package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import java.util.ArrayList;
import java.util.List;

/** Result receiver that collects the result in a list. */
public class InMemoryResultReceiver implements IResultReceiver {

	/** Result. */
	private final List<String> m_result;

	/** Constructor. */
	public InMemoryResultReceiver() {
		m_result = new ArrayList<>();
	}

	/** {@inheritDoc} */
	@Override
	public void append(String line) {
		m_result.add(line);
	}

	/** {@inheritDoc} */
	@Override
	public void append(List<String> lines) {
		m_result.addAll(lines);
	}

	/** {@inheritDoc} */
	@Override
	public void markResultAsPartiallyComplete() {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	public void markResultAsComplete() {
		// NOP
	}

	public List<String> getResult() {
		return m_result;
	}

	public boolean isEmpty() {
		return m_result.isEmpty();
	}
}
