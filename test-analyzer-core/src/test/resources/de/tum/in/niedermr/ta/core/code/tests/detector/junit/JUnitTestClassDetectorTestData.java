package de.tum.in.niedermr.ta.core.code.tests.detector.junit;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

public class JUnitTestClassDetectorTestData {

	public static class JUnit4TestClass {
		@Test
		public void a() {
			// NOP
		}

		@Test
		@Ignore
		public void ignored() {
			// NOP
		}
	}

	public static class JUnit3TestClass extends TestCase {

		public void testA() {
			// NOP
		}

		public void testButNoTestcase(String param) {
			param.toString();
		}
	}

	public static class JUnit4TestClassInheritingTestCase extends TestCase {
		@Test
		public void testA() {
			// NOP
		}
	}

	public static abstract class AbstractJUnit4TestClass {
		@Test
		public void a() {
			// NOP
		}
	}

	static class NonPublicTestClass {
		@Test
		public void a() {
			// NOP
		}
	}

	/**
	 * No annotation, no inheritance.
	 *
	 */
	public static class NoTestClass1 {
		public void testA() {
			// NOP
		}
	}

	public static class NoTestClass2 {
		@Ignore
		@Test
		public void ignored() {
			// NOP
		}
	}
}