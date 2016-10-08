package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import java.util.List;
import java.util.Objects;

/**
 * Result receiver that delegates the data to a specified receiver. Sub classes
 * can intercept the delegations.
 */
public class DelegationResultReceiver implements IResultReceiver {

	protected final IResultReceiver m_resultReceiver;

	/** Constructor. */
	public DelegationResultReceiver(IResultReceiver resultReceiver) {
		m_resultReceiver = Objects.requireNonNull(resultReceiver);
	}

	/** {@inheritDoc} */
	@Override
	public void append(String line) {
		m_resultReceiver.append(line);
	}

	/** {@inheritDoc} */
	@Override
	public void append(List<String> lines) {
		m_resultReceiver.append(lines);
	}

	/** {@inheritDoc} */
	@Override
	public void markResultAsPartiallyComplete() {
		m_resultReceiver.markResultAsPartiallyComplete();
	}

	/** {@inheritDoc} */
	@Override
	public void markResultAsComplete() {
		m_resultReceiver.markResultAsComplete();
	}
}
