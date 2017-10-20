package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.analysis.content.ClassFileData;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarModificationIterator;
import de.tum.in.niedermr.ta.core.code.iteration.IteratorException;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Iterator to instrument jar files. */
public class JarInstrumentationIterator extends JarModificationIterator {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(JarInstrumentationIterator.class);

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
			throws IteratorException, CodeOperationException, IOException {
		this.m_originalClassPath = originalClassPath;
		this.m_classBytes = cr.b;

		super.handleEntry(jarOperation, cr, originalClassPath);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleEntry(Throwable t, String className) throws IteratorException {
		if (m_operateFaultTolerant) {
			handleExceptionInHandleEntryFaultTolerant(t, className);
		} else {
			throw new IteratorException("Exception in handle entry", t);
		}
	}

	/** Fault tolerant exception handling. */
	private void handleExceptionInHandleEntryFaultTolerant(Throwable t, String className) {
		LOGGER.warn("Skipping bytecode instrumentation of " + JavaUtility.toClassName(className) + "! "
				+ "Fault tolerant mode permits to continue after " + t.getClass().getName() + " with message '"
				+ t.getMessage() + "'.");
		try {
			getJarFileWriter().writeClassIntoJar(new ClassFileData(m_originalClassPath, m_classBytes));
		} catch (IOException e) {
			LOGGER.error("Writiing class into jar failed: " + m_originalClassPath);
		}
	}
}