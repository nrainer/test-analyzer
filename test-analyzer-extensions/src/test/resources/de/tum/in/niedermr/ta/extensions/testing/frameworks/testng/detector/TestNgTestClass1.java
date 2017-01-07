package de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.detector;

public class TestNgTestClass1 {

	@org.testng.annotations.Test
	public void testX() {
		// NOP
	}

	public static class InnerClass {

		@Override
		public String toString() {
			return "InnerClass";
		}
	}
}
