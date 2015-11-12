package de.tum.in.niedermr.ta.runner.analysis.workflow;

import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.runner.analysis.AnalyzerRunnerInternal;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;

public abstract class AbstractWorkflow implements IWorkflow {
	protected static final Logger LOG = AnalyzerRunnerInternal.LOG;

	protected ExecutionInformation information;

	@Override
	public void init(String execId, Configuration configuration, String programPath, String workingFolder) {
		this.information = new ExecutionInformation(execId, configuration, programPath, workingFolder);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
}
