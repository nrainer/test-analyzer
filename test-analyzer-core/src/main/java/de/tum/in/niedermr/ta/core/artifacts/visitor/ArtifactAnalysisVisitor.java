package de.tum.in.niedermr.ta.core.artifacts.visitor;

import java.io.IOException;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;

/** Visitor for (read-only) analyzes. */
public class ArtifactAnalysisVisitor extends AbstractArtifactVisitor<ICodeAnalyzeOperation>
		implements IArtifactAnalysisVisitor {

	/** Constructor. */
	public ArtifactAnalysisVisitor(IArtifactIterator artifactIterator) {
		super(artifactIterator);
	}

	/** {@inheritDoc} */
	@Override
	public void execVisitClassEntry(ICodeAnalyzeOperation operation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException {
		operation.analyze(cr, originalClassPath);
	}
}
