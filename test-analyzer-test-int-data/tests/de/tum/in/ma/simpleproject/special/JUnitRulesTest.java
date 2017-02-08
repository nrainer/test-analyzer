package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class JUnitRulesTest {

	private static final SpecialTest2Object testObject = new SpecialTest2Object();

	@Rule
	public MyRule myRule = new MyRule();

	public class MyRule implements TestRule {
		@Override
		public Statement apply(Statement base, Description description) {
			return new MyStatement(base);
		}
	}

	public class MyStatement extends Statement {
		private final Statement base;

		public MyStatement(Statement base) {
			this.base = base;
		}

		@Override
		public void evaluate() throws Throwable {
			// must not be marked as covered

			testObject.callBefore();

			base.evaluate();

			// must not be marked as covered
			testObject.callAfter();
		}
	}

	@Test
	public void testRun() {
		assertEquals(5, testObject.callTest());
	}
}
