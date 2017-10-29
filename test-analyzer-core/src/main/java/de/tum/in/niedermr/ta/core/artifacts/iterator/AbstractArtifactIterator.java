package de.tum.in.niedermr.ta.core.artifacts.iterator;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

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

	protected abstract void beforeAll() throws IteratorException, IOException;

	protected abstract void handleEntry(OP artifactOperation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException;

	protected abstract void handleResource(OP artifactOperation, JarEntry resourceEntry, InputStream inStream)
			throws IteratorException, CodeOperationException, IOException;

	protected abstract void afterAll() throws IteratorException, IOException;
}
