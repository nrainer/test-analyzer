package de.tum.in.niedermr.ta.core.code.tests.runner.special;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;

/**
 * Takes only effect if the class implements {@link ITestRunner} too.
 */
public interface UsesOtherDetectorForTestcaseInstrumentation {
	public ITestClassDetector getTestClassDetectorForTestcaseInstrumentation(String[] testClassIncludes,
			String[] testClassExcludes);
}
