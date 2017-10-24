package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import java.util.Objects;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation.AnalysisInstrumentationOperation;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractInstrumentation;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Analysis instrumentation to compute the stack distance and invocation count. */
public class AnalysisInstrumentation extends AbstractInstrumentation {

	/** Class that records the data gathered from the instrumentation. */
	private Class<?> m_instrumentationDataRetrieverClass;

	public AnalysisInstrumentation(IFullExecutionId executionId, Class<?> instrumentationLoggerClass,
			boolean operateFaultTolerant) {
		super(executionId, operateFaultTolerant);
		m_instrumentationDataRetrieverClass = Objects.requireNonNull(instrumentationLoggerClass);
	}

	public void injectAnalysisStatements(String[] jarsToBeInstrumented, String genericJarOutputPath,
			ITestRunner testRunner, String[] testClassIncludes, String[] testClassExcludes) throws ExecutionException {
		// true as argument in order to include abstract test classes
		ITestClassDetector detector = testRunner.createTestClassDetector(true, testClassIncludes, testClassExcludes);
		AnalysisInstrumentationOperation operation = new AnalysisInstrumentationOperation(detector,
				m_instrumentationDataRetrieverClass);
		instrumentJars(jarsToBeInstrumented, genericJarOutputPath, operation);
	}
}
