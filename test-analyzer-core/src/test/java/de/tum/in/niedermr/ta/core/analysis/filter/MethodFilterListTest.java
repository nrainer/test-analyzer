package de.tum.in.niedermr.ta.core.analysis.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.sample.SampleClass;

/** Test {@link MethodFilterList}. */
public class MethodFilterListTest {

	/** Test. */
	@Test
	public void test() {
		MethodIdentifier methodIdentifier = MethodIdentifier.create(SampleClass.class, "getX", "()I");
		MethodNode methodNode = new MethodNode();

		MethodFilterList filterList = MethodFilterList.createEmpty();
		filterList.addFilter(new ConstantMethodFilter(true));

		FilterResult filterResult;

		filterResult = filterList.apply(methodIdentifier, methodNode);
		assertTrue(filterResult.isAccepted());

		filterList.addFilter(new ConstantMethodFilter(false));
		filterResult = filterList.apply(methodIdentifier, methodNode);
		assertFalse(filterResult.isAccepted());
		assertEquals("By definition", filterResult.getRejectReason());
	}

	/** Filter that returns a constant result. */
	private static class ConstantMethodFilter implements IMethodFilter {
		private boolean m_accept;

		/** Constructor. */
		private ConstantMethodFilter(boolean accept) {
			m_accept = accept;
		}

		/** {@inheritDoc} */
		@Override
		public FilterResult apply(MethodIdentifier methodIdentifier, MethodNode methodNode) {
			if (m_accept) {
				return FilterResult.accepted();
			}

			return FilterResult.reject(ConstantMethodFilter.class, "By definition");
		}
	}
}
