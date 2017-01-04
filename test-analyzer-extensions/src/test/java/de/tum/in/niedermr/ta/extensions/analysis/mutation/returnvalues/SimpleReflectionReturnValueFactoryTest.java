package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueFactory;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation.SampleClass2;

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

	/** Test. */
	@Test
	public void testSimpleConstructor() {
		Object obj = FACTORY.get(MethodIdentifier.EMPTY.get(), SampleClass2.class.getName());
		assertNotNull(obj);
		assertTrue(obj instanceof SampleClass2);
	}

	/** Test. */
	@Test
	public void testCreateInstanceWithSimpleParameters() {
		for (Constructor<?> constructor : SampleClass2.class.getConstructors()) {
			try {
				SimpleReflectionReturnValueFactory.createInstanceWithSimpleParameters(constructor);
			} catch (ReflectiveOperationException e) {
				fail("Instance creation with constructor " + constructor + " failed: " + e.getMessage());
			}
		}
	}
}
