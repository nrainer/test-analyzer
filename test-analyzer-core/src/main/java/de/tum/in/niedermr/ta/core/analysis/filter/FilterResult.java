package de.tum.in.niedermr.ta.core.analysis.filter;

/** Result of a filter operation. */
public final class FilterResult {
	private static final FilterResult ACCEPTED = new FilterResult(true, null, "");

	private final boolean m_accepted;
	private final Class<? extends IMethodFilter> m_methodFilter;
	private final String m_rejectReason;

	/** Constructor. */
	private FilterResult(boolean accepted, Class<? extends IMethodFilter> methodFilter, String rejectReason) {
		this.m_accepted = accepted;
		this.m_methodFilter = methodFilter;
		this.m_rejectReason = rejectReason;
	}

	public static FilterResult create(boolean accepted, Class<? extends IMethodFilter> methodFilter) {
		return new FilterResult(accepted, methodFilter, null);
	}

	public static FilterResult accepted() {
		return ACCEPTED;
	}

	public static FilterResult reject(Class<? extends IMethodFilter> filter) {
		return new FilterResult(false, filter, null);
	}

	public static FilterResult reject(Class<? extends IMethodFilter> filter, String reason) {
		return new FilterResult(false, filter, reason);
	}

	public boolean isAccepted() {
		return m_accepted;
	}

	public String getRejectReason() {
		return m_rejectReason;
	}

	/**
	 * Get the method filter. <br/>
	 * Note that the value is only set if {@link #isAccepted()} is false.
	 */
	public Class<? extends IMethodFilter> getMethodFilter() {
		return m_methodFilter;
	}

	@Override
	public String toString() {
		if (isAccepted()) {
			return "YES";
		} else {
			return "NO (" + getMethodFilter().getSimpleName()
					+ (getRejectReason() != null ? (": " + getRejectReason()) : "") + ")";
		}
	}
}
