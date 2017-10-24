package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Abstract iterator for jar files. */
public abstract class AbstractJarIterator<OP extends ICodeOperation> implements IArtifactIterator<OP> {
	private final String m_inputJarPath;
	private final IArtifactExceptionHandler m_exceptionHandler;

	public AbstractJarIterator(String inputJarPath, IArtifactExceptionHandler exceptionHandler) {
		m_inputJarPath = inputJarPath;
		m_exceptionHandler = exceptionHandler;
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
			m_exceptionHandler.onExceptionInArtifactIteration(t, this, jarOperation, getInputJarPath());
		}
	}

	private void processClassEntryList(OP jarOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getClassEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);
			ClassReader classInputReader = new ClassReader(inStream);
			String originalClassPath = entry.getName();

			try {
				handleEntry(jarOperation, classInputReader, originalClassPath);
			} catch (Throwable t) {
				m_exceptionHandler.onExceptionInHandleClass(t, this, classInputReader, originalClassPath);
			} finally {
				inStream.close();
			}
		}
	}

	private void processResourceEntryList(OP jarOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getResourceEntryList()) {
			InputStream inputStream = classContainer.getInputStream(entry);

			try {
				handleResource(jarOperation, entry, inputStream);
			} catch (Throwable t) {
				m_exceptionHandler.onExceptionInHandleResource(t, this, inputStream, entry.getName());
			} finally {
				inputStream.close();
			}
		}
	}

	protected abstract void beforeAll() throws IteratorException, IOException;

	protected abstract void handleEntry(OP jarOperation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException;

	protected abstract void handleResource(OP jarOperation, JarEntry resourceEntry, InputStream inStream)
			throws IteratorException, CodeOperationException, IOException;

	protected abstract void afterAll() throws IteratorException, IOException;
}
