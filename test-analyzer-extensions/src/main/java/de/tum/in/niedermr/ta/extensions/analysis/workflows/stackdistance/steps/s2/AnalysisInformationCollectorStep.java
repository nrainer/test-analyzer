package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s2;

import java.util.LinkedList;
import java.util.List;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisInformationCollector;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;

public class AnalysisInformationCollectorStep extends AbstractExecutionStep {
	private static final String EXEC_ID_ANALYSIS_COLLECTOR = "ANACOL";

	public AnalysisInformationCollectorStep(ExecutionInformation information) {
		super(information);
	}

	@Override
	public void runInternal() throws Exception {
		final String classPath = m_configuration.getTestAnalyzerClasspath().getValue() + CP_SEP
				+ getSourceInstrumentedJarFilesClasspath() + CP_SEP + m_configuration.getCodePathToTest().getValue()
				+ CP_SEP + m_configuration.getClasspath().getValue();

		List<String> arguments = new LinkedList<>();
		arguments.add(
				m_configuration.getCodePathToTest().getWithAlternativeSeparator(CommonConstants.SEPARATOR_DEFAULT));
		arguments.add(getFileInWorkingArea(AnalysisConstants.FILE_OUTPUT_ANALYSIS_INFORMATION));
		arguments.add(m_configuration.getTestRunner().getValue());
		arguments.add(m_configuration.getOperateFaultTolerant().getValueAsString());
		arguments.add(ProcessExecution.wrapPattern(m_configuration.getTestClassIncludes().getValue()));
		arguments.add(ProcessExecution.wrapPattern(m_configuration.getTestClassExcludes().getValue()));

		m_processExecution.execute(getFullExecId(EXEC_ID_ANALYSIS_COLLECTOR), ProcessExecution.NO_TIMEOUT,
				getClassNameToRun(), classPath, arguments);
	}

	protected String getClassNameToRun() {
		return AnalysisInformationCollector.class.getName();
	}

	protected String getSourceInstrumentedJarFilesClasspath() {
		return Environment.getClasspathOfIndexedFiles(
				getFileInWorkingArea(AnalysisConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), 0,
				m_configuration.getCodePathToMutate().countElements());
	}

	@Override
	protected String getDescription() {
		return "Analyzing stack distance";
	}
}
