package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source;

import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.TestInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.TestInstrumentationOperation;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * (INSTRU) Instruments all classes of a jar file by injecting logging statements into each method (except constructors
 * and static initializers). It is possible to skip test classes. Furthermore, it adds the class
 * de.tum.in.niedermr.ta.core.logic.instrumentation.InvocationLogger which holds the logging information.
 * 
 */
public class SourceInstrumentation extends AbstractInstrumentation {
	public SourceInstrumentation(IExecutionId executionId, boolean operateFaultTolerant) {
		super(executionId, operateFaultTolerant);
	}

	public void injectLoggingStatements(String[] jarsToBeInstrumented, String genericJarOutputPath,
			ITestRunner testRunner, String[] testClassIncludes, String[] testClassExcludes) throws ExecutionException {
		// true as argument in order not to instrument abstract test classes
		ITestClassDetector detector = testRunner.createTestClassDetector(true, testClassIncludes, testClassExcludes);

		TestInstrumentation testInstrumentation = new TestInstrumentation(getExecutionId(), isOperateFaultTolerant());

		// needed for test classes in source jars (which might be considered by the classloader first than in the
		// instrumented test jars)
		TestInstrumentationOperation testInstrumentationOperation = testInstrumentation
				.createTestInstrumentationOperation(testRunner, testClassIncludes, testClassExcludes);

		ICodeModificationOperation operation = new SourceInstrumentationOperation(detector,
				testInstrumentationOperation);
		instrumentJars(jarsToBeInstrumented, genericJarOutputPath, operation);
	}
}
