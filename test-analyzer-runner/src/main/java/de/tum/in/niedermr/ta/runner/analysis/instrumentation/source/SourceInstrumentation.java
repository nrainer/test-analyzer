package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source;

import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.TestInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.TestInstrumentationOperation;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

/**
 * (INSTRU) Instruments all classes of a jar file by injecting logging statements into each method (except constructors and static initializers). It is possible
 * to skip test classes. Furthermore, it adds the class de.tum.in.niedermr.ta.core.logic.instrumentation.InvocationLogger which holds the logging information.
 * 
 */
public class SourceInstrumentation extends AbstractInstrumentation {
	public SourceInstrumentation(String executionId, boolean operateFaultTolerant) {
		super(executionId, operateFaultTolerant);
	}

	public void injectLoggingStatements(String[] jarsToBeInstrumented, String genericJarOutputPath, ITestRunner testRunner) throws FailedExecution {
		// true as argument in order not to instrument abstract test classes
		ITestClassDetector detector = testRunner.getTestClassDetector(true);

		TestInstrumentation testInstrumentation = new TestInstrumentation(getExecutionId(), isOperateFaultTolerant());

		// needed for test classes in source jars (which might be considered by the classloader first than in the instrumented test jars)
		TestInstrumentationOperation testInstrumentationOperation = testInstrumentation.createTestInstrumentationOperation(testRunner);

		ICodeModificationOperation operation = new SourceInstrumentationOperation(detector, testInstrumentationOperation);
		instrumentJars(jarsToBeInstrumented, genericJarOutputPath, operation);
	}
}
