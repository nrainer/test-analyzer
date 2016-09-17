package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarModificationIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public abstract class AbstractInstrumentation {
	/** Logger. */
	static final Logger LOGGER = LogManager.getLogger(AbstractInstrumentation.class);

	private final IExecutionId m_executionId;
	private final boolean m_operateFaultTolerant;

	public AbstractInstrumentation(IExecutionId executionId, boolean operateFaultTolerant) {
		this.m_executionId = executionId;
		this.m_operateFaultTolerant = operateFaultTolerant;
	}

	public IExecutionId getExecutionId() {
		return m_executionId;
	}

	public boolean isOperateFaultTolerant() {
		return m_operateFaultTolerant;
	}

	protected void instrumentJars(String[] jarsToBeInstrumented, String genericJarOutputPath,
			ICodeModificationOperation operation) throws ExecutionException {
		try {
			for (int i = 0; i < jarsToBeInstrumented.length; i++) {
				JarModificationIterator jarWork = new JarInstrumentationIterator(jarsToBeInstrumented[i],
						Environment.getWithIndex(genericJarOutputPath, i), m_operateFaultTolerant);
				jarWork.execute(operation);
			}
		} catch (NoClassDefFoundError ex) {
			LOGGER.error("Incomplete classpath!");
			LOGGER.error(ex);
			throw new ExecutionException(m_executionId, ex);
		} catch (Throwable t) {
			LOGGER.error(t);
			throw new ExecutionException(m_executionId, t);
		}
	}
}