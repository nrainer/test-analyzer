package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

/** Statistics about the processing of methods. */
public class MethodProcessingStatistics {

	/** Method count. */
	private int m_methodCount = 0;

	/** Successfully processed methods. */
	private int m_successfulCount = 0;

	/** Skipped methods. */
	private int m_skippedCount = 0;

	/** Methods causing a timeout. */
	private int m_timeoutCount = 0;

	/** Methods with an error other than {@link #m_timeoutCount}. */
	private int m_errorCount = 0;

	/** {@link #m_methodCount} */
	public int getMethodCount() {
		return m_methodCount;
	}

	/** {@link #m_successfulCount} */
	public int getSuccessfulCount() {
		return m_successfulCount;
	}

	/** {@link #m_skippedCount} */
	public int getSkippedCount() {
		return m_skippedCount;
	}

	/** {@link #m_timeoutCount} */
	public int getTimeoutCount() {
		return m_timeoutCount;
	}

	/** {@link #m_errorCount} */
	public int getErrorCount() {
		return m_errorCount;
	}

	/** Increment {@link #m_methodCount} */
	public void incrementMethodCount() {
		m_methodCount++;
	}

	/** Increment {@link #m_successfulCount} */
	public void incrementSuccessfulCount() {
		m_successfulCount++;
	}

	/** Increment {@link #m_skippedCount} */
	public void incrementSkippedCount() {
		m_skippedCount++;
	}

	/** Increment {@link #m_timeoutCount} */
	public void incrementTimeoutCount() {
		m_timeoutCount++;
	}

	/** Increment {@link #m_errorCount} */
	public void incrementErrorCount() {
		m_errorCount++;
	}

	/** Add values from another instance. */
	public void add(MethodProcessingStatistics statistics) {
		m_methodCount += statistics.m_methodCount;
		m_successfulCount += statistics.m_successfulCount;
		m_skippedCount += statistics.m_skippedCount;
		m_timeoutCount += statistics.m_timeoutCount;
		m_errorCount += statistics.m_errorCount;
	}

	/** Get the summary. */
	public String toSummary() {
		return m_methodCount + " methods. " + m_successfulCount + " processed successfully. " + m_skippedCount
				+ " skipped. " + m_timeoutCount + " with timeout. " + m_errorCount + " failed.";
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "MethodProcessingStatistics [" + toSummary() + "]";
	}
}
