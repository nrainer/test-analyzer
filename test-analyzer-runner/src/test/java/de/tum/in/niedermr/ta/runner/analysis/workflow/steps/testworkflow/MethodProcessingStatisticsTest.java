package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Test {@link MethodProcessingStatistics}. */
public class MethodProcessingStatisticsTest {

	/** Test. */
	@Test
	public void test() {
		MethodProcessingStatistics statistics = new MethodProcessingStatistics();

		statistics.incrementMethodCount();
		statistics.incrementErrorCount();

		statistics.incrementMethodCount();
		statistics.incrementSkippedCount();

		statistics.incrementMethodCount();
		statistics.incrementSuccessfulCount();

		assertEquals("3 methods. 1 processed successfully. 1 skipped. 0 with timeout. 1 failed.",
				statistics.toSummary());

		MethodProcessingStatistics statistics2 = new MethodProcessingStatistics();
		statistics2.incrementMethodCount();
		statistics2.incrementSuccessfulCount();

		statistics2.incrementMethodCount();
		statistics2.incrementTimeoutCount();

		statistics.add(statistics2);
		assertEquals(5, statistics.getMethodCount());
		assertEquals(2, statistics.getSuccessfulCount());
		assertEquals("5 methods. 2 processed successfully. 1 skipped. 1 with timeout. 1 failed.",
				statistics.toSummary());
	}
}
