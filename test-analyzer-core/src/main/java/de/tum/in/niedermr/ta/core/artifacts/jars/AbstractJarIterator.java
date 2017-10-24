package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Abstract iterator for jar files. */
public abstract class AbstractJarIterator<OP extends ICodeOperation> implements IArtifactIterator<OP> {
	private final String m_inputJarPath;

	public AbstractJarIterator(String inputJarPath) {
		this.m_inputJarPath = inputJarPath;
	}

	/** @see #m_inputJarPath */
	public String getInputJarPath() {
		return m_inputJarPath;
	}

	/** {@inheritDoc} */
	@Override
	public final void execute(OP jarOperation) throws IteratorException {
		try {
			beforeAll();

			JarFileContent classContainer = JarFileContent.fromJarFile(m_inputJarPath);

			processClassEntryList(jarOperation, classContainer);
			processResourceEntryList(jarOperation, classContainer);

			afterAll();
		} catch (Throwable t) {
			onExceptionInJarProcessing(t, jarOperation);
		}
	}

	private void processClassEntryList(OP jarOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getClassEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);
			ClassReader cr = new ClassReader(inStream);

			try {
				handleEntry(jarOperation, cr, entry.getName());
			} catch (Throwable t) {
				onExceptionInHandleEntry(t, cr.getClassName());
			} finally {
				inStream.close();
			}
		}
	}

	private void processResourceEntryList(OP jarOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getResourceEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);

			try {
				handleResource(jarOperation, entry, inStream);
			} catch (Throwable t) {
				onExceptionInHandleResource(t, entry.getName());
			} finally {
				inStream.close();
			}
		}
	}

	protected abstract void beforeAll() throws IteratorException, IOException;

	protected abstract void handleEntry(OP jarOperation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException;

	protected abstract void handleResource(OP jarOperation, JarEntry resourceEntry, InputStream inStream)
			throws IteratorException, CodeOperationException, IOException;

	protected abstract void afterAll() throws IteratorException, IOException;

	protected abstract void onExceptionInHandleEntry(Throwable t, String className) throws IteratorException;

	protected abstract void onExceptionInHandleResource(Throwable t, String resourcePath) throws IteratorException;

	/**
	 * An exception occurred during the preparation or tear down (outside of
	 * entry or resource handling). <br/>
	 * {@link #beforeAll()} and {@link #afterAll()} may not have been invoked.
	 * 
	 * @param throwable
	 *            thrown exception
	 * @param jarOperation
	 *            operation to be executed
	 */
	protected abstract void onExceptionInJarProcessing(Throwable throwable, OP jarOperation) throws IteratorException;
}
