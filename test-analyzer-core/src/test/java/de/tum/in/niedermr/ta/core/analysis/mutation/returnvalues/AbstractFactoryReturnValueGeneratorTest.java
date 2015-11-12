package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

@SuppressWarnings("unused")
public class AbstractFactoryReturnValueGeneratorTest {
	@Test
	public void testInstanceWithValidFactory() throws Exception {
		try {
			new AbstractFactoryReturnValueGenerator(ValidFactory1.class) {
				// NOP
			};
		} catch (Exception ex) {
			fail();
		}
	}

	@Test(expected = Exception.class)
	public void testInstanceWithInvalidFactory1() throws Exception {
		new AbstractFactoryReturnValueGenerator(InvalidFactory1.class) {
			// NOP
		};
	}

	@Test(expected = Exception.class)
	public void testInstanceWithInvalidFactory2() throws Exception {
		new AbstractFactoryReturnValueGenerator(InvalidFactory2.class) {
			// NOP
		};
	}

	@Test(expected = Exception.class)
	public void testInstanceWithInvalidFactory3() throws Exception {
		new AbstractFactoryReturnValueGenerator(InvalidFactory3.class) {
			// NOP
		};
	}

	private static class ValidFactory1 extends AbstractReturnFactory {
		public static final ValidFactory1 INSTANCE = new ValidFactory1();

		@Override
		public Object getWithException(MethodIdentifier methodIdentifier, String returnType)
				throws NoSuchElementException {
			return null;
		}
	}

	/**
	 * Has no field instance.
	 */
	private static class InvalidFactory1 extends AbstractReturnFactory {
		@Override
		public Object getWithException(MethodIdentifier methodIdentifier, String returnType)
				throws NoSuchElementException {
			return null;
		}
	}

	/**
	 * Instance is not static and not public.
	 */
	private static class InvalidFactory2 extends AbstractReturnFactory {
		private final InvalidFactory2 INSTANCE = new InvalidFactory2();

		@Override
		public Object getWithException(MethodIdentifier methodIdentifier, String returnType)
				throws NoSuchElementException {
			return null;
		}
	}

	/**
	 * Wrong type of instance.
	 */
	private static class InvalidFactory3 extends AbstractReturnFactory {
		public static final AbstractReturnFactory INSTANCE = new InvalidFactory3();

		@Override
		public Object getWithException(MethodIdentifier methodIdentifier, String returnType)
				throws NoSuchElementException {
			return null;
		}
	}
}
