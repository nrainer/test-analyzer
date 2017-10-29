package de.tum.in.niedermr.ta.core.artifacts.factory;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.DefaultIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.FaultTolerantIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.artifacts.visitor.ArtifactAnalysisVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.ArtifactModificationVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactAnalysisVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactModificationVisitor;

public abstract class AbstractArtifactVisitorFactory implements IArtifactVisitorFactory {

	/** {@inheritDoc} */
	@Override
	public final IArtifactAnalysisVisitor createAnalyzeVisitor(String artifactContainerInputPath,
			boolean faultTolerant) {
		IArtifactExceptionHandler exceptionHandler = createArtifactExceptionHandler(faultTolerant);
		return createAnalyzeVisitor(artifactContainerInputPath, exceptionHandler);
	}

	@Override
	public IArtifactAnalysisVisitor createAnalyzeVisitor(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler) {
		IArtifactIterator artifactIterator = createArtifactIterator(artifactContainerInputPath, exceptionHandler);
		return new ArtifactAnalysisVisitor(artifactIterator);
	}

	/** {@inheritDoc} */
	@Override
	public final IArtifactModificationVisitor createModificationVisitor(String artifactContainerInputPath,
			String artifactContainerOutputPath, boolean faultTolerant) {
		IArtifactExceptionHandler exceptionHandler = createArtifactExceptionHandler(faultTolerant);
		return createModificationVisitor(artifactContainerInputPath, artifactContainerOutputPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactModificationVisitor createModificationVisitor(String artifactContainerInputPath,
			String artifactContainerOutputPath, IArtifactExceptionHandler exceptionHandler) {
		IArtifactIterator artifactIterator = createArtifactIterator(artifactContainerInputPath, exceptionHandler);
		IArtifactOutputWriter artifactOutputWriter = createArtifactOutputWriter(artifactContainerOutputPath);
		return new ArtifactModificationVisitor(artifactIterator, artifactOutputWriter);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactExceptionHandler createArtifactExceptionHandler(boolean faultTolerant) {
		if (faultTolerant) {
			return new FaultTolerantIteratorExceptionHandler();
		}

		return new DefaultIteratorExceptionHandler();
	}
}
