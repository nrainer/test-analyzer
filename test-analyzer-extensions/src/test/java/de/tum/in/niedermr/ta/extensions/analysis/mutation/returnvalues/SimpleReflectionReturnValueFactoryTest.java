package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.TestAbortReason;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation.SampleClass2;

/** Test {@link SimpleReflectionReturnValueFactory}. */
public class SimpleReflectionReturnValueFactoryTest extends AbstractInstancesReturnValueFactoryTest {

	public SimpleReflectionReturnValueFactoryTest() {
		super(SimpleReflectionReturnValueFactory.INSTANCE);
	}

	/** Test. */
	@Test
	public void testCreateArray() {
		Object obj = m_factory.get(MethodIdentifier.EMPTY.get(),
				SimpleReflectionReturnValueFactoryTest.class.getName() + "[]");
		assertNotNull(obj);
		assertTrue(obj.getClass().isArray());

		Object multiDimensionalArray = SimpleReflectionReturnValueFactory.createArray(Object.class.getName() + "[][][]",
				Object.class);
		assertEquals(new Object[0][][].getClass(), multiDimensionalArray.getClass());
	}

	/** Test. */
	@Test
	public void testCreateEnumArray() {
		Object obj = m_factory.get(MethodIdentifier.EMPTY.get(), TestAbortReason.class.getName() + "[]");
		assertNotNull(obj);
		assertTrue(obj.getClass().isArray());
	}

	/** Test. */
	@Test
	public void testEnumValue() {
		Object obj = m_factory.get(MethodIdentifier.EMPTY.get(), TestAbortReason.class.getName());
		assertNotNull(obj);
		assertEquals(TestAbortReason.TEST_DIED, obj);
	}

	/** Test. */
	@Test
	public void testTryFindInstanceCreationMethod() throws ReflectiveOperationException {
		Object obj = SimpleReflectionReturnValueFactory.tryFindInstanceCreationMethod(SampleClass2.class);
		assertNotNull(obj);
		assertTrue(obj instanceof SampleClass2);
	}

	/** Test. */
	@Test
	public void testParameterlessConstructor() {
		Object obj = m_factory.get(MethodIdentifier.EMPTY.get(),
				SimpleReflectionReturnValueFactoryTest.class.getName());
		assertNotNull(obj);
		assertTrue(obj instanceof SimpleReflectionReturnValueFactoryTest);
	}

	/** Test. */
	@Test
	public void testSimpleConstructor() {
		Object obj = m_factory.get(MethodIdentifier.EMPTY.get(), SampleClass2.class.getName());
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
