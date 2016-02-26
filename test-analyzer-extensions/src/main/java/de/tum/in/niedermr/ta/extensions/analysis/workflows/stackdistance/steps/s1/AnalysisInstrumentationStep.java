package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s1;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.instrumentation.AnalysisInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;

/**
 * Note that it is ok to log invocations from framing methods (@Before) too because they also invoke the mutated
 * methods.
 */
public class AnalysisInstrumentationStep extends AbstractExecutionStep {
	private static final String EXEC_ID_ANALYSIS_INSTRUMENTATION = "ANAINS";

	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws Exception {
		/**
		 * Instrument the methods of the jars to compute the min and max stack distance to the testcase.
		 */

		final String executionId = getFullExecId(EXEC_ID_ANALYSIS_INSTRUMENTATION);
		final boolean operateFaultTolerant = configuration.getOperateFaultTolerant().getValue();
		ITestRunner testRunner = configuration.getTestRunner().createInstance();

		AnalysisInstrumentation analysisInstrumentation = new AnalysisInstrumentation(executionId,
				operateFaultTolerant);
		analysisInstrumentation.injectAnalysisStatements(configuration.getCodePathToMutate().getElements(),
				getFileInWorkingArea(AnalysisConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), testRunner,
				configuration.getTestClassIncludes().getElements(), configuration.getTestClassExcludes().getElements());
	}

	@Override
	protected String getDescription() {
		return "Instrumenting jar file for analysis";
	}
}