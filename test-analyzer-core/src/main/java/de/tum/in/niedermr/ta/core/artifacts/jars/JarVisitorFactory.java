package de.tum.in.niedermr.ta.core.artifacts.jars;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.factory.AbstractArtifactVisitorFactory;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;

/** Factory to create instances of iterators. */
public class JarVisitorFactory extends AbstractArtifactVisitorFactory {

	/** {@inheritDoc} */
	@Override
	public IArtifactOutputWriter createArtifactOutputWriter(String jarFile) {
		return new JarFileWriter(jarFile);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactIterator createArtifactIterator(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler) {
		return new JarIterator(artifactContainerInputPath, exceptionHandler);
	}
}
