package de.tum.in.niedermr.ta.core.artifacts.binaryclasses;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.factory.AbstractArtifactVisitorFactory;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;

/** Factory to create instances of visitors. */
public class BinaryClassesVisitorFactory extends AbstractArtifactVisitorFactory {

	/** {@inheritDoc} */
	@Override
	public IArtifactOutputWriter createArtifactOutputWriter(String artifactContainerPath) {
		return new BinaryClassesFileWriter(artifactContainerPath);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactIterator createArtifactIterator(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler) {
		return new BinaryClassesIterator(artifactContainerInputPath, exceptionHandler);
	}
}
