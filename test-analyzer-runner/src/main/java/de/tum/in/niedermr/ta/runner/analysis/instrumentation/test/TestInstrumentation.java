package de.tum.in.niedermr.ta.runner.analysis.instrumentation.test;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.tests.runner.special.UsesOtherDetectorForTestcaseInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractInstrumentation;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * The test instrumentation modifies testcases. At the beginning of each testcase an instruction to set the mode of the
 * invocation logger to testing is executed and at the end of the testcase the mode is reset. This needs to be done in
 * order find out which methods of the source code are really invoked by testcases (and which are invoked by the set up
 * or tear down of testcases).
 * 
 */
public class TestInstrumentation extends AbstractInstrumentation {
	public TestInstrumentation(String executionId, boolean operateFaultTolerant) {
		super(executionId, operateFaultTolerant);
	}

	public void injectTestingModeStatements(String[] jarsWithTests, String genericJarOutputPath, ITestRunner testRunner,
			String[] testClassIncludes, String[] testClassExcludes) throws ExecutionException {
		TestInstrumentationOperation operation = createTestInstrumentationOperation(testRunner, testClassIncludes,
				testClassExcludes);
		instrumentJars(jarsWithTests, genericJarOutputPath, operation);
	}

	public TestInstrumentationOperation createTestInstrumentationOperation(ITestRunner testRunner,
			String[] testClassIncludes, String[] testClassExcludes) {
		ITestClassDetector detector = getAppropriateTestDetector(testRunner, testClassIncludes, testClassExcludes);
		return new TestInstrumentationOperation(detector);
	}

	protected ITestClassDetector getAppropriateTestDetector(ITestRunner testRunner, String[] testClassIncludes,
			String[] testClassExcludes) {
		if (testRunner instanceof UsesOtherDetectorForTestcaseInstrumentation) {
			return ((UsesOtherDetectorForTestcaseInstrumentation) testRunner)
					.getTestClassDetectorForTestcaseInstrumentation(testClassIncludes, testClassExcludes);
		} else {
			// true as argument in order to get also testcases in abstract classes
			return testRunner.createTestClassDetector(true, testClassIncludes, testClassExcludes);
		}
	}
}
