package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueFactory;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Test {@link SimpleReflectionReturnValueFactory}. */
public class SimpleReflectionReturnValueFactoryTest {

	private static final AbstractReturnValueFactory FACTORY = SimpleReflectionReturnValueFactory.INSTANCE;

	/** Test. */
	@Test
	public void testCreateArray() {
		Object obj = FACTORY.get(MethodIdentifier.EMPTY.get(),
				SimpleReflectionReturnValueFactoryTest.class.getName() + "[]");
		assertNotNull(obj);
		assertTrue(obj.getClass().isArray());
	}

	/** Test. */
	@Test
	public void testEnumValue() {
		Object obj = FACTORY.get(MethodIdentifier.EMPTY.get(), TestAbortReason.class.getName());
		assertNotNull(obj);
		assertEquals(TestAbortReason.TEST_DIED, obj);
	}

	/** Test. */
	@Test
	public void testParameterlessConstructor() {
		Object obj = FACTORY.get(MethodIdentifier.EMPTY.get(), SimpleReflectionReturnValueFactoryTest.class.getName());
		assertNotNull(obj);
		assertTrue(obj instanceof SimpleReflectionReturnValueFactoryTest);
	}
}
