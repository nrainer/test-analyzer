package de.tum.in.niedermr.ta.core.artifacts.visitor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.artifacts.content.ClassFileData;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;

/** Visitor for modifications in Jar files. */
public class ArtifactModificationVisitor extends AbstractArtifactVisitor<ICodeModificationOperation>
		implements IArtifactModificationVisitor {
	private final IArtifactOutputWriter m_artifactOutputWriter;

	/** Constructor. */
	public ArtifactModificationVisitor(IArtifactIterator artifactIterator, IArtifactOutputWriter artifactOutputWriter) {
		super(artifactIterator);
		m_artifactOutputWriter = artifactOutputWriter;
	}

	public IArtifactOutputWriter getArtifactOutputWriter() {
		return m_artifactOutputWriter;
	}

	/** {@inheritDoc} */
	@Override
	public void execVisitClassEntry(ICodeModificationOperation jarOperation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException {
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

		jarOperation.modify(cr, cw);

		byte[] transformedClass = cw.toByteArray();
		m_artifactOutputWriter.writeClass(new ClassFileData(originalClassPath, transformedClass));
	}

	/** {@inheritDoc} */
	@Override
	public void execVisitResourceEntry(ICodeModificationOperation jarOperation, InputStream inStream, String entryName)
			throws IteratorException, CodeOperationException, IOException {
		m_artifactOutputWriter.writeResource(new ClassFileData(entryName, IOUtils.toByteArray(inStream)));
	}

	/** {@inheritDoc} */
	@Override
	public void visitAfterAll() throws IteratorException, IOException {
		m_artifactOutputWriter.ensureAllStreamsClosed();
	}
}
