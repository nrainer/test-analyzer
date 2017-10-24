package de.tum.in.niedermr.ta.core.artifacts.jars;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.AbstractArtifactIteratorFactory;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactAnalysisIterator;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactModificationIterator;

/** Factory to create instances of iterators. */
public class JarIteratorFactory extends AbstractArtifactIteratorFactory {

	/** {@inheritDoc} */
	@Override
	public IArtifactAnalysisIterator createAnalyzeIterator(String inputJarPath,
			IArtifactExceptionHandler exceptionHandler) {
		return new JarAnalyzeIterator(inputJarPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactModificationIterator createModificationIterator(String inputJarPath, String outputJarPath,
			IArtifactExceptionHandler exceptionHandler) {
		return new JarModificationIterator(inputJarPath, outputJarPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactOutputWriter createArtifactOutputWriter(String jarFile) {
		return new JarFileWriter(jarFile);
	}
}
