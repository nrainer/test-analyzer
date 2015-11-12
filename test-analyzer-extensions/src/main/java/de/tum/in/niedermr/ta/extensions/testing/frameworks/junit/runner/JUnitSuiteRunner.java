package de.tum.in.niedermr.ta.extensions.testing.frameworks.junit.runner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.tests.runner.junit.JUnitTestRunResult;
import de.tum.in.niedermr.ta.core.code.tests.runner.junit.JUnitTestRunner;
import de.tum.in.niedermr.ta.core.code.tests.runner.special.UsesOtherDetectorForTestcaseInstrumentation;
import de.tum.in.niedermr.ta.core.code.tests.runner.special.UsesOwnCollector;
import de.tum.in.niedermr.ta.extensions.testing.frameworks.junit.collector.JUnitSuiteCollector;
import de.tum.in.niedermr.ta.extensions.testing.frameworks.junit.detector.JUnitSuiteDetector;

public class JUnitSuiteRunner extends JUnitTestRunner implements ITestRunner, UsesOwnCollector, UsesOtherDetectorForTestcaseInstrumentation {
	private static final Pattern TEST_INDEX = Pattern.compile("T_(\\d*)_");

	@Override
	public JUnitTestRunResult runTest(Class<?> testClass, String testcaseName) {
		junit.framework.Test test = getTestByName(testClass, testcaseName);

		return new JUnitTestRunResult(jUnitCore.run(test));
	}

	@Override
	public JUnitSuiteDetector getTestClassDetector(boolean acceptAbstractTestClasses, String... ignoredTestClassRegexes) {
		return new JUnitSuiteDetector(ignoredTestClassRegexes);
	}

	@Override
	public JUnitSuiteCollector getTestCollector(boolean acceptAbstractTestClasses, String... ignoredTestClassRegexes) {
		return new JUnitSuiteCollector(getTestClassDetector(acceptAbstractTestClasses, ignoredTestClassRegexes));
	}

	private junit.framework.Test getTestByName(Class<?> testClass, String testcaseName) {
		JUnitSuiteCollector collector = getTestCollector(false);
		List<junit.framework.Test> tests = collector.getTestsOfSuite(testClass);

		return tests.get(getTestIndex(testcaseName));
	}

	private int getTestIndex(String testcaseName) {
		Matcher matcher = TEST_INDEX.matcher(testcaseName);

		matcher.find();

		return Integer.parseInt(matcher.group(1));
	}

	@Override
	public ITestClassDetector getTestClassDetectorForTestcaseInstrumentation() {
		return new JUnitTestClassDetector(true);
	}
}
