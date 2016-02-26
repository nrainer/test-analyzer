package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s2;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.source.SourceInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.TestInstrumentation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;

public class InstrumentationStep extends AbstractExecutionStep {
	private static final String EXEC_ID_INSTRUMENTATION = "INSTRU";

	public InstrumentationStep(ExecutionContext information) {
		super(information);
	}

	@Override
	public void runInternal() throws Exception {
		/**
		 * Instrument the methods of the jars to be mutated in order to find the methods under test to be mutated out of
		 * these.
		 */

		final String executionId = getFullExecId(EXEC_ID_INSTRUMENTATION);
		final boolean operateFaultTolerant = m_configuration.getOperateFaultTolerant().getValue();
		ITestRunner testRunner = m_configuration.getTestRunner().createInstance();

		SourceInstrumentation sourceInstrumentation = new SourceInstrumentation(executionId, operateFaultTolerant);
		sourceInstrumentation.injectLoggingStatements(m_configuration.getCodePathToMutate().getElements(),
				getFileInWorkingArea(FILE_TEMP_JAR_INSTRUMENTED_SOURCE_X), testRunner,
				m_configuration.getTestClassIncludes().getElements(),
				m_configuration.getTestClassExcludes().getElements());

		TestInstrumentation testInstrumentation = new TestInstrumentation(executionId, operateFaultTolerant);
		testInstrumentation.injectTestingModeStatements(m_configuration.getCodePathToTest().getElements(),
				getFileInWorkingArea(FILE_TEMP_JAR_INSTRUMENTED_TEST_X), testRunner,
				m_configuration.getTestClassIncludes().getElements(),
				m_configuration.getTestClassExcludes().getElements());
	}

	@Override
	protected String getDescription() {
		return "Instrumenting jar file";
	}
}
