package de.tum.in.niedermr.ta.core.code.tests.runner;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.special.UsesOtherDetectorForTestcaseInstrumentation;
import de.tum.in.niedermr.ta.core.code.tests.runner.special.UsesOwnCollector;

/**
 * 
 * @see UsesOwnCollector
 * @see UsesOtherDetectorForTestcaseInstrumentation
 */
public interface ITestRunner {
	public ITestClassDetector getTestClassDetector(boolean acceptAbstractTestClasses, String[] testClassIncludes,
			String[] testClassExcludes);

	public ITestRunResult runTest(Class<?> testClass, String testcaseName);

	public void runTestsWithoutResult(Class<?> cls);
}
