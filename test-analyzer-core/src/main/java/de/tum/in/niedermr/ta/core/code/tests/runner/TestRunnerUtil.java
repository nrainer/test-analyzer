package de.tum.in.niedermr.ta.core.code.tests.runner;

import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.core.code.tests.collector.TestCollector;
import de.tum.in.niedermr.ta.core.code.tests.runner.special.UsesOwnCollector;

public class TestRunnerUtil {
	public static ITestCollector getAppropriateTestCollector(ITestRunner testRunner, boolean acceptAbstractClasses,
			String[] testClassIncludes, String[] testClassExcludes) {
		if (testRunner instanceof UsesOwnCollector) {
			return ((UsesOwnCollector) testRunner).getTestCollector(acceptAbstractClasses, testClassIncludes,
					testClassExcludes);
		} else {
			return new TestCollector(
					testRunner.getTestClassDetector(acceptAbstractClasses, testClassIncludes, testClassExcludes));
		}
	}
}
