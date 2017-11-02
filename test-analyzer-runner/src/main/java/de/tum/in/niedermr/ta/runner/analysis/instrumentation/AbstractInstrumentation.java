package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.factory.MainArtifactVisitorFactory;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactModificationVisitor;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

public abstract class AbstractInstrumentation {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractInstrumentation.class);

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
				IArtifactModificationVisitor modificationVisitor = createModificationVisitor(jarsToBeInstrumented,
						genericJarOutputPath, i);
				modificationVisitor.execute(operation);
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

	protected IArtifactModificationVisitor createModificationVisitor(String[] jarsToBeInstrumented,
			String genericJarOutputPath, int index) {
		String inputArtifactPath = jarsToBeInstrumented[index];
		String outputArtifactPath = Environment.getWithIndex(genericJarOutputPath, index);
		IArtifactExceptionHandler exceptionHandler;

		if (m_operateFaultTolerant) {
			exceptionHandler = new FaultTolerantInstrumentationIteratorExceptionHandler();
		} else {
			exceptionHandler = MainArtifactVisitorFactory.INSTANCE
					.createArtifactExceptionHandler(m_operateFaultTolerant);
		}

		return MainArtifactVisitorFactory.INSTANCE.createModificationVisitor(inputArtifactPath, outputArtifactPath,
				exceptionHandler);
	}
}