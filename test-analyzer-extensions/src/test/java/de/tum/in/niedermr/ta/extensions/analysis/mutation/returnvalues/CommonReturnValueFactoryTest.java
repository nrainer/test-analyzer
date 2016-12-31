package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class CommonReturnValueFactoryTest {
	private static final String NOT_SUPPORTED_CLASS = "java.unsupported.UnsupportedClass";
	private static final String JAVA_UTIL_LIST = "java.util.List";

	private static final CommonReturnValueFactory FACTORY = CommonReturnValueFactory.INSTANCE;

	/** Test. */
	@Test
	public void testObjectsAreNew() {
		List<?> list1 = (List<?>) FACTORY.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST);
		List<?> list2 = (List<?>) FACTORY.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST);

		assertFalse(list1 == list2);
	}

	/** Test. */
	@Test
	public void testSomeObjects() {
		Object obj;

		obj = FACTORY.get(MethodIdentifier.EMPTY.get(), "java.lang.Comparable");
		assertNotNull(obj);
		assertTrue(obj instanceof Comparable<?>);

		obj = FACTORY.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST);
		assertNotNull(obj);
		assertTrue(obj instanceof List<?>);

		obj = FACTORY.get(MethodIdentifier.EMPTY.get(), "java.io.File");
		assertNotNull(obj);
		assertTrue(obj instanceof File);

		obj = FACTORY.get(MethodIdentifier.EMPTY.get(), "java.math.BigInteger");
		assertNotNull(obj);
		assertTrue(obj instanceof BigInteger);

		obj = FACTORY.get(MethodIdentifier.EMPTY.get(), "int[]");
		assertNotNull(obj);
		assertTrue(obj instanceof int[]);

		obj = FACTORY.get(MethodIdentifier.EMPTY.get(), "java.lang.Integer[]");
		assertNotNull(obj);
		assertTrue(obj instanceof Integer[]);
	}

	/** Test. */
	@Test
	public void testSupports() {
		boolean supported;

		supported = FACTORY.supports(MethodIdentifier.EMPTY, JAVA_UTIL_LIST);
		assertTrue(supported == (FACTORY.get(MethodIdentifier.EMPTY.get(), JAVA_UTIL_LIST) != null));

		supported = FACTORY.supports(MethodIdentifier.EMPTY, NOT_SUPPORTED_CLASS);
		assertTrue(supported == (FACTORY.get(MethodIdentifier.EMPTY.get(), NOT_SUPPORTED_CLASS) != null));
	}

	/** Test. */
	@Test
	public void testSimpleReflectionFallback() {
		Object obj = FACTORY.get(MethodIdentifier.EMPTY.get(), CommonReturnValueFactoryTest.class.getName());
		assertNotNull(obj);
		assertTrue(obj instanceof CommonReturnValueFactoryTest);
	}

	/** Test. */
	@Test(expected = NoSuchElementException.class)
	public void testThrowsException() {
		FACTORY.getWithException(MethodIdentifier.EMPTY, NOT_SUPPORTED_CLASS);
	}
}
