package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/** Execution step to write the result to a file. */
public class PersistResultStep extends AbstractExecutionStep {
	private static final String RESULT_FILE = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "code-statistics"
			+ FILE_EXTENSION_SQL_TXT;

	private final List<String> m_result = new LinkedList<>();
	private IResultPresentationExtended m_resultPresentation;

	@Override
	protected void execInitialized(ExecutionContext context) {
		super.execInitialized(context);
		m_resultPresentation = IResultPresentationExtended.create(context.getExecutionId());
	}

	@Override
	protected String getSuffixForFullExecutionId() {
		return "PERDAT";
	}

	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {
		TextFileData.writeToFile(getFileInWorkingArea(RESULT_FILE), m_result);
	}

	public void addResultInstructionsPerMethod(Map<MethodIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> m_result
				.add(m_resultPresentation.formatInstructionsPerMethod(entry.getKey(), entry.getValue())));
	}

	public void addResultModifierPerMethod(Map<MethodIdentifier, String> codeInformation) {
		codeInformation.entrySet().forEach(
				entry -> m_result.add(m_resultPresentation.formatModifierPerMethod(entry.getKey(), entry.getValue())));
	}

	public void addResultInstructionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> m_result
				.add(m_resultPresentation.formatInstructionsPerTestcase(entry.getKey(), entry.getValue())));
	}

	public void addResultAssertionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> m_result
				.add(m_resultPresentation.formatAssertionsPerTestcase(entry.getKey(), entry.getValue())));
	}

	@Override
	protected String getDescription() {
		return "Persisting the result in a single file";
	}
}
