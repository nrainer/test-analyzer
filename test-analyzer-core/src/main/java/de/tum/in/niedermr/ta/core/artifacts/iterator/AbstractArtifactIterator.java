package de.tum.in.niedermr.ta.core.artifacts.iterator;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Abstract iterator. */
public abstract class AbstractArtifactIterator<OP extends ICodeOperation> implements IArtifactIterator<OP> {
	private final String m_pathToResource;
	private final IArtifactExceptionHandler m_exceptionHandler;

	/** Constructor. */
	public AbstractArtifactIterator(String pathToResource, IArtifactExceptionHandler exceptionHandler) {
		m_pathToResource = pathToResource;
		m_exceptionHandler = exceptionHandler;
	}

	/** @see #m_pathToResource */
	public String getPathToResource() {
		return m_pathToResource;
	}

	/** @see #m_exceptionHandler */
	public IArtifactExceptionHandler getExceptionHandler() {
		return m_exceptionHandler;
	}

	/** {@inheritDoc} */
	@Override
	public final void execute(OP artifactOperation) throws IteratorException {
		try {
			beforeAll();
			processArtifactContent(artifactOperation);
			afterAll();
		} catch (Throwable t) {
			m_exceptionHandler.onExceptionInArtifactIteration(t, this, artifactOperation, getPathToResource());
		}
	}

	protected abstract void processArtifactContent(OP artifactOperation) throws IOException, IteratorException;

	protected void beforeAll() throws IteratorException, IOException {
		// NOP
	}

	protected void handleEntry(OP artifactOperation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException {
		// NOP
	}

	protected void handleResource(OP artifactOperation, InputStream inStream, String entryName)
			throws IteratorException, CodeOperationException, IOException {
		// NOP
	}

	protected void afterAll() throws IteratorException, IOException {
		// NOP
	}

	protected void processClassEntry(OP artifactOperation, InputStream inStream, String originalClassPath)
			throws IOException, IteratorException {
		ClassReader classInputReader = new ClassReader(inStream);

		try {
			handleEntry(artifactOperation, classInputReader, originalClassPath);
		} catch (Throwable t) {
			getExceptionHandler().onExceptionInHandleClass(t, this, classInputReader, originalClassPath);
		} finally {
			inStream.close();
		}
	}

	protected void processResourceEntry(OP artifactOperation, InputStream inputStream, String entryName)
			throws IteratorException, IOException {
		try {
			handleResource(artifactOperation, inputStream, entryName);
		} catch (Throwable t) {
			getExceptionHandler().onExceptionInHandleResource(t, this, inputStream, entryName);
		} finally {
			inputStream.close();
		}
	}
}
