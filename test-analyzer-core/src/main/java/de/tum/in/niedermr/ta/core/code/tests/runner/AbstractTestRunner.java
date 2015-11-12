package de.tum.in.niedermr.ta.core.code.tests.runner;

public abstract class AbstractTestRunner implements ITestRunner {
	@Override
	public final ITestRunResult runTest(Class<?> testClass, String testcaseName) {
		ITestRunResult result;

		before();
		result = runTestInternal(testClass, testcaseName);
		after();

		return result;
	}

	protected abstract void before();

	protected abstract ITestRunResult runTestInternal(Class<?> testClass, String testcaseName);

	protected abstract void after();
}
