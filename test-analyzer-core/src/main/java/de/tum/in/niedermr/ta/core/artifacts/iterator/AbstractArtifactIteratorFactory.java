package de.tum.in.niedermr.ta.core.artifacts.iterator;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.DefaultIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.FaultTolerantIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;

public abstract class AbstractArtifactIteratorFactory implements IArtifactIteratorFactory {

	/** {@inheritDoc} */
	@Override
	public final IArtifactAnalysisIterator createAnalyzeIterator(String artifactContainerInputPath,
			boolean faultTolerant) {
		return createAnalyzeIterator(artifactContainerInputPath, createArtifactExceptionHandler(faultTolerant));
	}

	/** {@inheritDoc} */
	@Override
	public final IArtifactModificationIterator createModificationIterator(String artifactContainerInputPath,
			String artifactContainerOutputPath, boolean faultTolerant) {
		return createModificationIterator(artifactContainerInputPath, artifactContainerOutputPath,
				createArtifactExceptionHandler(faultTolerant));
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
