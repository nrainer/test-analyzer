package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.analysis.jars.content.JarFileElementRawData;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarModificationIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

public class AbstractInstrumentation {
	private static final Logger LOG = LogManager.getLogger(AbstractInstrumentation.class);

	private final String m_executionId;
	private final boolean m_operateFaultTolerant;

	public AbstractInstrumentation(String executionId, boolean operateFaultTolerant) {
		this.m_executionId = executionId;
		this.m_operateFaultTolerant = operateFaultTolerant;
	}

	public String getExecutionId() {
		return m_executionId;
	}

	public boolean isOperateFaultTolerant() {
		return m_operateFaultTolerant;
	}

	protected void instrumentJars(String[] jarsToBeInstrumented, String genericJarOutputPath,
			ICodeModificationOperation operation) throws FailedExecution {
		try {
			for (int i = 0; i < jarsToBeInstrumented.length; i++) {
				JarModificationIterator jarWork = new JarInstrumentationIterator(jarsToBeInstrumented[i],
						Environment.getWithIndex(genericJarOutputPath, i));
				jarWork.execute(operation);
			}
		} catch (NoClassDefFoundError ex) {
			LOG.error("Incomplete classpath!");
			LOG.error(ex);
			throw new FailedExecution(m_executionId, ex);
		} catch (Throwable t) {
			LOG.error(t);
			throw new FailedExecution(m_executionId, t);
		}
	}

	class JarInstrumentationIterator extends JarModificationIterator {
		private String m_originalClassPath;
		private byte[] m_classBytes;

		public JarInstrumentationIterator(String inputJarPath, String outputJarPath) {
			super(inputJarPath, outputJarPath);
		}

		@Override
		protected void handleEntry(ICodeModificationOperation jarOperation, ClassReader cr, String originalClassPath)
				throws Exception {
			this.m_originalClassPath = originalClassPath;
			this.m_classBytes = cr.b;

			super.handleEntry(jarOperation, cr, originalClassPath);
		}

		@Override
		protected void onExceptionInHandleEntry(Throwable t, String className) throws Exception {
			if (isOperateFaultTolerant()) {
				getJarFileWriter().writeClassIntoJar(new JarFileElementRawData(m_originalClassPath, m_classBytes));
				LOG.warn("Skipping bytecode instrumentation of " + JavaUtility.toClassName(className) + "! "
						+ "Fault tolerant mode permits to continue after " + t.getClass().getName() + " with message '"
						+ t.getMessage() + "'.");
			} else {
				throw new Exception(t);
			}
		}
	}
}