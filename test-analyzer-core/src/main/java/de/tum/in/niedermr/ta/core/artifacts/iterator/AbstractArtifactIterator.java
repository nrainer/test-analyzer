package de.tum.in.niedermr.ta.core.artifacts.iterator;

import java.io.IOException;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactVisitorForIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Abstract iterator. */
public abstract class AbstractArtifactIterator implements IArtifactIterator {
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

	/** {@inheritDoc} */
	@Override
	public IArtifactExceptionHandler getExceptionHandler() {
		return m_exceptionHandler;
	}

	/** {@inheritDoc} */
	@Override
	public <OP extends ICodeOperation> void iterate(IArtifactVisitorForIterator<OP> visitor, OP artifactOperation)
			throws IteratorException {
		try {
			visitor.visitBeforeAll();
			processArtifactContent(visitor, artifactOperation);
			visitor.visitAfterAll();
		} catch (Throwable t) {
			m_exceptionHandler.onExceptionInArtifactIteration(t, visitor, artifactOperation, getPathToResource());
		}
	}

	protected abstract <OP extends ICodeOperation> void processArtifactContent(IArtifactVisitorForIterator<OP> visitor,
			OP artifactOperation) throws IOException, IteratorException;
}
