package de.tum.in.niedermr.ta.core.artifacts.visitor;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Abstract iterator. */
public abstract class AbstractArtifactVisitor<OP extends ICodeOperation>
		implements IArtifactVisitor<OP>, IArtifactVisitorForIterator<OP> {

	private IArtifactExceptionHandler m_exceptionHandler;
	private IArtifactIterator m_artifactIterator;

	/** Constructor. */
	public AbstractArtifactVisitor(IArtifactIterator artifactIterator) {
		m_artifactIterator = artifactIterator;
		m_exceptionHandler = m_artifactIterator.getExceptionHandler();
	}

	/** {@inheritDoc} */
	@Override
	public void execute(OP operation) throws IteratorException {
		m_artifactIterator.iterate(this, operation);
	}

	/** {@inheritDoc} */
	@Override
	public void visitBeforeAll() throws IteratorException, IOException {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	public void visitAfterAll() throws IteratorException, IOException {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	public void visitClassEntry(OP artifactOperation, InputStream inStream, String originalClassPath)
			throws IOException, IteratorException {
		ClassReader classInputReader = new ClassReader(inStream);

		try {
			execVisitClassEntry(artifactOperation, classInputReader, originalClassPath);
		} catch (Throwable t) {
			m_exceptionHandler.onExceptionInHandleClass(t, this, classInputReader, originalClassPath);
		} finally {
			inStream.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void visitResourceEntry(OP artifactOperation, InputStream inputStream, String entryName)
			throws IteratorException, IOException {
		try {
			execVisitResourceEntry(artifactOperation, inputStream, entryName);
		} catch (Throwable t) {
			m_exceptionHandler.onExceptionInHandleResource(t, this, inputStream, entryName);
		} finally {
			inputStream.close();
		}
	}

	/**
	 * @param artifactOperation
	 * @param cr
	 * @param originalClassPath
	 */
	protected void execVisitClassEntry(OP artifactOperation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException {
		// NOP
	}

	/***
	 * @param artifactOperation
	 * @param inStream
	 * @param entryName
	 */
	protected void execVisitResourceEntry(OP artifactOperation, InputStream inStream, String entryName)
			throws IteratorException, CodeOperationException, IOException {
		// NOP
	}
}
