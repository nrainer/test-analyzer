package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.Map;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.SimplePersistResultStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/** Execution step to write the result to a file. */
public class PersistResultStep extends SimplePersistResultStep {
	private static final String RESULT_FILE = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "code-statistics"
			+ FILE_EXTENSION_SQL_TXT;

	private IResultPresentationExtended m_resultPresentation;

	/** {@inheritDoc} */
	@Override
	protected void execInitialized(ExecutionContext context) {
		super.execInitialized(context);
		setResultFileName(RESULT_FILE);
		m_resultPresentation = IResultPresentationExtended.create(context.getExecutionId());
	}

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "PERDAT";
	}

	public void addResultInstructionsPerMethod(Map<MethodIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> appendToResult(
				m_resultPresentation.formatInstructionsPerMethod(entry.getKey(), entry.getValue())));
	}

	public void addResultModifierPerMethod(Map<MethodIdentifier, String> codeInformation) {
		codeInformation.entrySet().forEach(entry -> appendToResult(
				m_resultPresentation.formatModifierPerMethod(entry.getKey(), entry.getValue())));
	}

	public void addResultInstructionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> appendToResult(
				m_resultPresentation.formatInstructionsPerTestcase(entry.getKey(), entry.getValue())));
	}

	public void addResultAssertionsPerTestcase(Map<MethodIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> appendToResult(
				m_resultPresentation.formatAssertionsPerTestcase(entry.getKey(), entry.getValue())));
	}
}
