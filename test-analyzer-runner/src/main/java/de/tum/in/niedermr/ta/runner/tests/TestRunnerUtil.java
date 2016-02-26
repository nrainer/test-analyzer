package de.tum.in.niedermr.ta.runner.tests;

import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.core.code.tests.collector.TestCollector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.tests.runner.special.UsesOwnCollector;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;

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

	/** Create the test class detector. */
	public static ITestClassDetector getTestClassDetector(Configuration configuration, boolean acceptAbstractClasses)
			throws ReflectiveOperationException {
		ITestRunner testRunner = configuration.getTestRunner().createInstance();
		return testRunner.getTestClassDetector(acceptAbstractClasses,
				configuration.getTestClassIncludes().getElements(), configuration.getTestClassExcludes().getElements());
	}
}
