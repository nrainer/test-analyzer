package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Test {@link CommonReturnValueFactory}. */
public class CommonReturnValueFactoryTest extends AbstractInstancesReturnValueFactoryTest {

	private static final String NOT_SUPPORTED_CLASS = "java.unsupported.UnsupportedClass";
	private static final String JAVA_UTIL_LIST = "java.util.List";

	/** Constructor. */
	public CommonReturnValueFactoryTest() {
		super(CommonReturnValueFactory.INSTANCE);
	}

	/** Test. */
	@Test
	public void testObjectsAreNew() {
		List<?> list1 = (List<?>) m_factory.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST);
		List<?> list2 = (List<?>) m_factory.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST);

		assertFalse(list1 == list2);
	}

	/** Test. */
	@Test
	public void testSomeObjects() {
		Object obj;

		obj = m_factory.get(MethodIdentifier.EMPTY.get(), "java.lang.Comparable");
		assertNotNull(obj);
		assertTrue(obj instanceof Comparable<?>);

		obj = m_factory.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST);
		assertNotNull(obj);
		assertTrue(obj instanceof List<?>);

		obj = m_factory.get(MethodIdentifier.EMPTY.get(), "java.io.File");
		assertNotNull(obj);
		assertTrue(obj instanceof File);

		obj = m_factory.get(MethodIdentifier.EMPTY.get(), "java.math.BigInteger");
		assertNotNull(obj);
		assertTrue(obj instanceof BigInteger);

		obj = m_factory.get(MethodIdentifier.EMPTY.get(), "int[]");
		assertNotNull(obj);
		assertTrue(obj instanceof int[]);

		obj = m_factory.get(MethodIdentifier.EMPTY.get(), "java.lang.Integer[]");
		assertNotNull(obj);
		assertTrue(obj instanceof Integer[]);

		obj = m_factory.get(MethodIdentifier.EMPTY.get(), Class.class.getName());
		assertNotNull(obj);
		assertTrue(obj instanceof Class);
	}

	/** Test. */
	@Test
	public void testSupports() {
		boolean supported;

		supported = m_factory.supports(MethodIdentifier.EMPTY, JAVA_UTIL_LIST);
		assertTrue(supported == (m_factory.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST) != null));

		supported = m_factory.supports(MethodIdentifier.EMPTY, NOT_SUPPORTED_CLASS);
		assertTrue(supported == (m_factory.get(MethodIdentifier.EMPTY.get(), NOT_SUPPORTED_CLASS) != null));
	}

	/** Test. */
	@Test
	public void testFallback() {
		Object obj = m_factory.get(MethodIdentifier.EMPTY.get(), CommonReturnValueFactory.class.getName());
		assertNotNull(obj);
		assertTrue(obj instanceof CommonReturnValueFactory);
	}

	/** Test. */
	@Test
	public void testUnsupportedClass() {
		assertClassIsUnsupported(NOT_SUPPORTED_CLASS);
	}
}
