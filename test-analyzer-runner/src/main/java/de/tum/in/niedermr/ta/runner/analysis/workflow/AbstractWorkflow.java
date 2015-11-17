package de.tum.in.niedermr.ta.runner.analysis.workflow;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;

public abstract class AbstractWorkflow implements IWorkflow {

	protected ExecutionInformation m_information;

	@Override
	public void init(String execId, Configuration configuration, String programPath, String workingFolder) {
		this.m_information = new ExecutionInformation(execId, configuration, programPath, workingFolder);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
}
