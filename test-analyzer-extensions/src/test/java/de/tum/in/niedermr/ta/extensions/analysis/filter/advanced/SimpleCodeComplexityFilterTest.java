package de.tum.in.niedermr.ta.extensions.analysis.filter.advanced;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterTest;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.sample.SampleClass;

/** Test {@link SimpleCodeComplexityFilter}. */
public class SimpleCodeComplexityFilterTest {

	/** Test. */
	@Test
	public void testSimpleCodeComplexityFilter() throws Exception {
		final String className = SampleClass.class.getName();

		IMethodFilter filter1 = new SimpleCodeComplexityFilter(4);
		IMethodFilter filter2 = new SimpleCodeComplexityFilter(5);
		IMethodFilter filter3 = new SimpleCodeComplexityFilter(6);

		MethodNode methodNode = new MethodNode();
		methodNode.maxLocals = 3;
		methodNode.maxStack = 2;

		assertTrue(FilterTest.analyze(filter1, className, "setX", "(I)V", methodNode));
		assertFalse(FilterTest.analyze(filter2, className, "setX", "(I)V", methodNode));
		assertFalse(FilterTest.analyze(filter3, className, "setX", "(I)V", methodNode));
	}
}
