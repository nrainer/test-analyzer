package de.tum.in.niedermr.ta.runner.analysis.workflow;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;

public abstract class AbstractWorkflow implements IWorkflow {

	protected ExecutionContext m_context;

	@Override
	public void init(String execId, Configuration configuration, String programPath, String workingFolder) {
		this.m_context = new ExecutionContext(execId, configuration, programPath, workingFolder);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
}
