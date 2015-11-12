package de.tum.in.niedermr.ta.core.code.tests.runner.junit;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;

public class JUnitTestRunner implements ITestRunner {
	protected final JUnitCore jUnitCore;

	public JUnitTestRunner() {
		this.jUnitCore = new JUnitCore();
	}

	@Override
	public JUnitTestRunResult runTest(Class<?> testClass, String testcaseName) {
		Result result = jUnitCore.run(Request.method(testClass, testcaseName));

		return new JUnitTestRunResult(result);
	}

	@Override
	public void runTestsWithoutResult(Class<?> cls) {
		jUnitCore.run(cls);
	}

	@Override
	public ITestClassDetector getTestClassDetector(boolean acceptAbstractTestClasses, String... ignoredTestClassRegexes) {
		return new JUnitTestClassDetector(acceptAbstractTestClasses, ignoredTestClassRegexes);
	}
}
