package de.tum.in.niedermr.ta.core.code.tests.runner.special;

import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;

/**
 * Takes only effect if the class implements {@link ITestRunner} too.
 */
public interface UsesOwnCollector {
	public ITestCollector getTestCollector(boolean acceptAbstractTestClasses, String... ignoredTestClassRegexes);
}
