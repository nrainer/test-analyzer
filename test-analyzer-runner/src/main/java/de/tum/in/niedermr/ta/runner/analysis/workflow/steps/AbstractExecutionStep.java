package de.tum.in.niedermr.ta.runner.analysis.workflow.steps;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.runner.factory.FactoryUtil;

/** Base class for an execution step. */
public abstract class AbstractExecutionStep implements IExecutionStep, EnvironmentConstants {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractExecutionStep.class);

	private boolean m_initialized;

	private ExecutionContext m_context;
	private Configuration m_configuration;
	private ProcessExecution m_processExecution;

	private long m_startTimeInMs = -1;

	/** {@inheritDoc} */
	@Override
	public final void initialize(ExecutionContext context) throws ExecutionException {
		this.m_context = context;
		this.m_configuration = context.getConfiguration();
		this.m_processExecution = FactoryUtil.createFactory(context.getConfiguration()).createNewProcessExecution(
				context.getWorkingFolder(), context.getProgramPath(), context.getWorkingFolder());
		execInitialized(context);
		m_initialized = true;
	}

	/**
	 * The initialization was executed, {@link #m_initialized} will be set to <code>true</code> after this method.
	 * 
	 * @param context
	 */
	protected void execInitialized(ExecutionContext context) {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	public final void start() throws ExecutionException {
		if (!m_initialized) {
			throw new ExecutionException(ExecutionIdFactory.NOT_SPECIFIED, "Not initialized");
		}

		try {
			LOGGER.info("START: " + getDescription());

			m_startTimeInMs = System.currentTimeMillis();
			runInternal(m_configuration, m_processExecution);
			long duration = getDurationSinceStartInMs();

			LOGGER.info("COMPLETED: " + getDescription());
			LOGGER.info("DURATION: " + duration + " seconds.");
		} catch (ExecutionException ex) {
			throw ex;
		} catch (Throwable t) {
			throw new ExecutionException(getExecutionId(), t);
		}
	}

	protected abstract void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, ReflectiveOperationException, IOException;

	/** Get the description of this step. */
	protected abstract String getDescription();

	/** {@link #m_context} */
	public ExecutionContext getContext() {
		return m_context;
	}

	/** Get the execution id. */
	protected final IExecutionId getExecutionId() {
		return m_context.getExecutionId();
	}

	/** Create the full execution id for this step. */
	protected final IFullExecutionId createFullExecutionId() {
		return getExecutionId().createFullExecutionId(getSuffixForFullExecutionId());
	}

	/** Get the duration in ms since the execution start. Return -1 if the step has not been started yet. */
	protected final long getDurationSinceStartInMs() {
		if (m_startTimeInMs == -1) {
			return -1;
		}

		return CommonUtility.getDuration(m_startTimeInMs);
	}

	/** Get the step-specific suffix to create the full execution id. */
	protected abstract String getSuffixForFullExecutionId();

	protected final String getWithIndex(String fileName, int index) {
		return Environment.getWithIndex(fileName, index);
	}

	protected final String getFileInWorkingArea(String fileName) {
		return AbstractWorkflow.getFileInWorkingArea(m_context, fileName);
	}

	@Override
	public String toString() {
		return "ExecutionStep [" + getDescription() + "]";
	}
}
