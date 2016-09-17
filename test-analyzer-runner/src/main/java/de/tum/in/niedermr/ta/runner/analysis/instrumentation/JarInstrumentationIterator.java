package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.analysis.jars.content.JarFileElementRawData;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarModificationIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Iterator to instrument jar files. */
public class JarInstrumentationIterator extends JarModificationIterator {
	private final boolean m_operateFaultTolerant;
	private String m_originalClassPath;
	private byte[] m_classBytes;

	/** Constructor. */
	public JarInstrumentationIterator(String inputJarPath, String outputJarPath, boolean operateFaultTolerant) {
		super(inputJarPath, outputJarPath);
		this.m_operateFaultTolerant = operateFaultTolerant;
	}

	/** {@inheritDoc} */
	@Override
	protected void handleEntry(ICodeModificationOperation jarOperation, ClassReader cr, String originalClassPath)
			throws Exception {
		this.m_originalClassPath = originalClassPath;
		this.m_classBytes = cr.b;

		super.handleEntry(jarOperation, cr, originalClassPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleEntry(Throwable t, String className) throws Exception {
		if (m_operateFaultTolerant) {
			getJarFileWriter().writeClassIntoJar(new JarFileElementRawData(m_originalClassPath, m_classBytes));
			AbstractInstrumentation.LOGGER.warn("Skipping bytecode instrumentation of "
					+ JavaUtility.toClassName(className) + "! " + "Fault tolerant mode permits to continue after "
					+ t.getClass().getName() + " with message '" + t.getMessage() + "'.");
		} else {
			throw new Exception(t);
		}
	}
}