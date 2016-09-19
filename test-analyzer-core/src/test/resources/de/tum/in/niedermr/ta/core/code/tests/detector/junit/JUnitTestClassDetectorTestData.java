package de.tum.in.niedermr.ta.core.code.tests.detector.junit;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

public class JUnitTestClassDetectorTestData {

	static class JUnit4TestClass {
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

	static class JUnit3TestClass extends TestCase {
		public void testA() {
			// NOP
		}
	}

	static abstract class AbstractJUnit4TestClass {
		@Test
		public void a() {
			// NOP
		}
	}

	/**
	 * No annotation, no inheritance.
	 *
	 */
	static class NoTestClass1 {
		public void testA() {
			// NOP
		}
	}

	static class NoTestClass2 {
		@Ignore
		@Test
		public void ignored() {
			// NOP
		}
	}
}
