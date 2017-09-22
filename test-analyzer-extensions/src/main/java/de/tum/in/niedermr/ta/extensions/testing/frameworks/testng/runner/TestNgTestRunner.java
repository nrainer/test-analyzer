package de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.runner;

import java.util.LinkedList;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.extensions.testing.frameworks.testng.detector.TestNgTestClassDetector;

public class TestNgTestRunner implements ITestRunner {
	@Override
	public ITestRunResult runTest(Class<?> testClass, String testcaseName) {
		TestNG testng = new TestNG();
		TestListenerAdapter listener = new TestListenerAdapter();

		testng.setTestClasses(new Class[] { testClass });
		testng.setMethodInterceptor(new SelectMethodByNameInterceptor(testcaseName));
		testng.addListener(listener);

		testng.run();

		return new TestNgRunResult(listener);
	}

	class SelectMethodByNameInterceptor implements IMethodInterceptor {
		private final String m_methodName;

		public SelectMethodByNameInterceptor(String methodName) {
			this.m_methodName = methodName;
		}

		@Override
		public List<IMethodInstance> intercept(List<IMethodInstance> methodInstances, ITestContext context) {
			List<IMethodInstance> selection = new LinkedList<>();

			for (IMethodInstance currentMethodInstance : methodInstances) {
				if (currentMethodInstance.getMethod().getMethodName().equals(m_methodName)) {
					selection.add(currentMethodInstance);
				}
			}

			return selection;
		}
	}

	@Override
	public ITestClassDetector createTestClassDetector(boolean acceptAbstractTestClasses, String[] testClassIncludes,
			String[] testClassExcludes) {
		return new TestNgTestClassDetector(acceptAbstractTestClasses, testClassIncludes, testClassExcludes);
	}
}
