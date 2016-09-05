package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps;

import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;

/** Step to parse coverage files. */
public class CoverageParserStep extends AbstractExecutionStep {

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "COVPAR";
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {

	}

	public void setCoverageFileName(String coverageFileName) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Parse coverage files";
	}
}
