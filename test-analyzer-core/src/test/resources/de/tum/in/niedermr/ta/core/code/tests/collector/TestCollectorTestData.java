package de.tum.in.niedermr.ta.core.code.tests.collector;

import org.junit.Test;

public class TestCollectorTestData {

	class EmptyClass {
		// NOP
	}

	class TestClass1 {
		@Test
		public void a() {
			// NOP
		}

		@Test
		public void b() {
			// NOP
		}
	}

	class TestClass2 {
		@Test
		public void a() {
			// NOP
		}

		@Test
		public void c() {
			// NOP
		}
	}

	abstract class AbstractTestClassA {
		@Test
		public void a() {
			// NOP
		}
	}

	abstract class AbstractTestClassB extends AbstractTestClassA {
		// NOP
	}

	class NonAbstractTestClassC extends AbstractTestClassB {
		@Test
		public void b() {
			// NOP
		}
	}

	class InheritingTestClass extends NonAbstractTestClassC {
		@Test
		public void c() {
			// NOP
		}
	}
}
