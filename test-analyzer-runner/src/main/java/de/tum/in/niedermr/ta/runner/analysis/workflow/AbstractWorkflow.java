package de.tum.in.niedermr.ta.runner.analysis.workflow;

import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.IExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

public abstract class AbstractWorkflow implements IWorkflow {

	private ExecutionContext m_context;

	@Override
	public void init(String execId, Configuration configuration, String programPath, String workingFolder) {
		m_context = new ExecutionContext(execId, configuration, programPath, workingFolder);
	}

	@Override
	public void start() throws FailedExecution {
		if (m_context == null) {
			throw new FailedExecution("UNKNOWN", "Not initialized");
		}

		startInternal(m_context, m_context.getConfiguration());
	}

	protected abstract void startInternal(ExecutionContext context, Configuration configuration) throws FailedExecution;

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	protected <T extends IExecutionStep> T createAndInitializeExecutionStep(Class<T> executionStepClass)
			throws FailedExecution {
		try {
			T executionStep = executionStepClass.newInstance();
			executionStep.initialize(m_context);
			return executionStep;
		} catch (Exception e) {
			throw new FailedExecution(m_context.getExecutionId(), e);
		}
	}
}
