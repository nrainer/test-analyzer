package de.tum.in.niedermr.ta.core.code.tests.collector;

import org.junit.Test;

public class TestCollectorTestData {

	public static class EmptyClass {
		// NOP
	}

	public static class TestClass1 {
		@Test
		public void a() {
			// NOP
		}

		@Test
		public void b() {
			// NOP
		}
	}

	public static class TestClass2 {
		@Test
		public void a() {
			// NOP
		}

		@Test
		public void c() {
			// NOP
		}
	}

	public static abstract class AbstractTestClassA {
		@Test
		public void a() {
			// NOP
		}
	}

	public static abstract class AbstractTestClassB extends AbstractTestClassA {
		// NOP
	}

	public static class NonAbstractTestClassC extends AbstractTestClassB {
		@Test
		public void b() {
			// NOP
		}
	}

	public static class InheritingTestClass extends NonAbstractTestClassC {
		@Test
		public void c() {
			// NOP
		}
	}
}
