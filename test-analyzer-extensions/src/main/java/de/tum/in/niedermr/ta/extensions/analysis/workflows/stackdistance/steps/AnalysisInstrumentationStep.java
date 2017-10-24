package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation.AnalysisInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * Instrument the methods of the jars to compute the min and max stack distance to the testcase.<br/>
 * Note that it is ok to log invocations from framing methods (@Before) too because they also invoke the mutated
 * methods.
 */
public class AnalysisInstrumentationStep extends AbstractExecutionStep {

	/** Class that records the the data from the instrumented classes. */
	private Class<?> m_recorderClass;

	/** {@link #m_recorderClass} */
	public void setStackLogRecorderClass(Class<?> recorderClass) {
		m_recorderClass = recorderClass;
	}

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "ANAINS";
	}

	/** {@inheritDoc} */
	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, ReflectiveOperationException {
		boolean operateFaultTolerant = configuration.getOperateFaultTolerant().getValue();
		ITestRunner testRunner = configuration.getTestRunner().createInstance();

		AnalysisInstrumentation analysisInstrumentation = new AnalysisInstrumentation(createFullExecutionId(),
				m_recorderClass, operateFaultTolerant);
		analysisInstrumentation.injectAnalysisStatements(configuration.getCodePathToMutate().getElements(),
				getFileInWorkingArea(ExtensionEnvironmentConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), testRunner,
				configuration.getTestClassIncludes().getElements(), configuration.getTestClassExcludes().getElements());
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Instrumenting jar file for analysis";
	}
}