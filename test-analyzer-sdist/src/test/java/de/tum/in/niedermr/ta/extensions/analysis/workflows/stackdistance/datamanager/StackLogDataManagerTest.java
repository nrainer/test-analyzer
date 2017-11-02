package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.sample.SampleClass;

/** Test {@link StackLogDataManager}. */
public class StackLogDataManagerTest {

	/** Test. */
	@Test
	public void testManager() {
		StackLogDataManager.resetAndStart(TestcaseIdentifier.create("CommonTest", "testcase1"));

		MethodIdentifier method1 = MethodIdentifier.create(SampleClass.class, "m1", "()V");
		MethodIdentifier method2 = MethodIdentifier.create(SampleClass.class, "m2", "()V");

		StackLogDataManager.visitMethodInvocation(method1, 3);
		StackLogDataManager.visitMethodInvocation(method1, 4);
		StackLogDataManager.visitMethodInvocation(method2, 1);
		StackLogDataManager.visitMethodInvocation(method2, 8);
		StackLogDataManager.visitMethodInvocation(method1, 2);

		assertEquals(3, (int) StackLogDataManager.getInvocationsCount().get(method1));
		assertEquals(2, (int) StackLogDataManager.getInvocationsCount().get(method2));

		assertEquals(2, (int) StackLogDataManager.getInvocationsMinDistance().get(method1));
		assertEquals(4, (int) StackLogDataManager.getInvocationsMaxDistance().get(method1));
	}
}
