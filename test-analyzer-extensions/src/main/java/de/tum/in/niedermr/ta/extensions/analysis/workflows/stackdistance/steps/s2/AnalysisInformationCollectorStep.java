package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s2;

import java.util.LinkedList;
import java.util.List;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AnalysisInformationCollector;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;

public class AnalysisInformationCollectorStep extends AbstractExecutionStep {
	private static final String EXEC_ID_ANALYSIS_COLLECTOR = "ANACOL";

	@Override
	public void runInternal(Configuration configuration, ProcessExecution processExecution) throws Exception {
		final String classPath = configuration.getTestAnalyzerClasspath().getValue() + CP_SEP
				+ getSourceInstrumentedJarFilesClasspath(configuration) + CP_SEP
				+ configuration.getCodePathToTest().getValue() + CP_SEP + configuration.getClasspath().getValue();

		List<String> arguments = new LinkedList<>();
		arguments.add(configuration.getCodePathToTest().getWithAlternativeSeparator(CommonConstants.SEPARATOR_DEFAULT));
		arguments.add(getFileInWorkingArea(AnalysisConstants.FILE_OUTPUT_ANALYSIS_INFORMATION));
		arguments.add(configuration.getTestRunner().getValue());
		arguments.add(configuration.getOperateFaultTolerant().getValueAsString());
		arguments.add(ProcessExecution.wrapPattern(configuration.getTestClassIncludes().getValue()));
		arguments.add(ProcessExecution.wrapPattern(configuration.getTestClassExcludes().getValue()));

		processExecution.execute(getFullExecId(EXEC_ID_ANALYSIS_COLLECTOR), ProcessExecution.NO_TIMEOUT,
				getClassNameToRun(), classPath, arguments);
	}

	protected String getClassNameToRun() {
		return AnalysisInformationCollector.class.getName();
	}

	protected String getSourceInstrumentedJarFilesClasspath(Configuration configuration) {
		return Environment.getClasspathOfIndexedFiles(
				getFileInWorkingArea(AnalysisConstants.FILE_TEMP_JAR_ANALYSIS_INSTRUMENTED_SOURCE_X), 0,
				configuration.getCodePathToMutate().countElements());
	}

	@Override
	protected String getDescription() {
		return "Analyzing stack distance";
	}
}
