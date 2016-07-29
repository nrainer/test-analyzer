package de.tum.in.niedermr.ta.runner.analysis.workflow;

import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.IExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

public abstract class AbstractWorkflow implements IWorkflow {

	private ExecutionContext m_context;

	@Override
	public void initWorkflow(ExecutionContext executionContext) {
		m_context = executionContext;
	}

	@Override
	public final void start() throws ExecutionException {
		if (m_context == null) {
			throw new ExecutionException(ExecutionIdFactory.NOT_SPECIFIED, "Not initialized");
		}

		startInternal(m_context, m_context.getConfiguration());
	}

	protected abstract void startInternal(ExecutionContext context, Configuration configuration)
			throws ExecutionException;

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	protected <T extends IExecutionStep> T createAndInitializeExecutionStep(Class<T> executionStepClass)
			throws ExecutionException {
		try {
			T executionStep = executionStepClass.newInstance();
			executionStep.initialize(m_context);
			return executionStep;
		} catch (Exception e) {
			throw new ExecutionException(m_context.getExecutionId(), e);
		}
	}
}
