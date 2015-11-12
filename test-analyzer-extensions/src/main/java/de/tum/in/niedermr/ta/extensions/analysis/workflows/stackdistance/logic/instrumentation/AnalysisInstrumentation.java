package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.instrumentation;

import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.AbstractInstrumentation;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

public class AnalysisInstrumentation extends AbstractInstrumentation {
	public AnalysisInstrumentation(String executionId, boolean operateFaultTolerant) {
		super(executionId, operateFaultTolerant);
	}

	public void injectAnalysisStatements(String[] jarsToBeInstrumented, String genericJarOutputPath, ITestRunner testRunner) throws FailedExecution {
		// true as argument in order to include abstract test classes
		ITestClassDetector detector = testRunner.getTestClassDetector(true);
		AnalysisInstrumentationOperation operation = new AnalysisInstrumentationOperation(detector);
		instrumentJars(jarsToBeInstrumented, genericJarOutputPath, operation);
	}
}
