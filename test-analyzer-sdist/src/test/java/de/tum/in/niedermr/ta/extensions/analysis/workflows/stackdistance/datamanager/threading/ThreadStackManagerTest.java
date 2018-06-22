package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.threading;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation.SampleClass;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.threading.ThreadStackManager;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v2.StackLogRecorderV2;
import de.tum.in.niedermr.ta.sample.SampleClassExtended;

/** Test {@link ThreadStackManager}. */
public class ThreadStackManagerTest {

	/** Test. */
	@Test
	public void testComputeStackHeightOfStackTrace() {
		StackTraceElement[] stackTrace = new StackTraceElement[8];
		stackTrace[0] = new StackTraceElement(ThreadStackManager.class.getName(), "computeCurrentStackHeight", "", 10);
		stackTrace[1] = new StackTraceElement(StackLogRecorderV2.class.getName(), "pushInvocation", "", 10);
		stackTrace[2] = new StackTraceElement(Integer.class.getName(), "valueOf", "", 10);
		stackTrace[3] = new StackTraceElement(SampleClass.class.getName(), "getInt", "", 20);
		stackTrace[4] = new StackTraceElement(SampleClass.class.getName(), "getInt", "", 20);
		stackTrace[5] = new StackTraceElement(SampleClassExtended.class.getName(), "getLongConst1", "", 20);
		stackTrace[6] = new StackTraceElement(SampleClassExtended.class.getName(), "getCalculation", "", 10);
		stackTrace[7] = new StackTraceElement(Thread.class.getName(), "run", "", 10);

		assertEquals(5, ThreadStackManager.computeStackHeightOfStackTrace(StackLogRecorderV2.class.getName(),
				Thread.class.getName(), stackTrace, new String[0]));

		assertEquals(2,
				ThreadStackManager.computeStackHeightOfStackTrace(StackLogRecorderV2.class.getName(),
						Thread.class.getName(), stackTrace,
						new String[] { "java.lang.", SampleClassExtended.class.getName() }));
	}
}
