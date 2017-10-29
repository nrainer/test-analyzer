package de.tum.in.niedermr.ta.core.artifacts.iterator;

import de.tum.in.niedermr.ta.core.artifacts.binaryclasses.BinaryClassesIteratorFactory;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.jars.JarIteratorFactory;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public final class MainArtifactIteratorFactory extends AbstractArtifactIteratorFactory {

	/** Instance that delegates to the appropriate factory. */
	public static final IArtifactIteratorFactory INSTANCE = new MainArtifactIteratorFactory();

	private final JarIteratorFactory m_jarIteratorFactory;
	private final BinaryClassesIteratorFactory m_binaryClassesIteratorFactory;

	/** Constructor. */
	public MainArtifactIteratorFactory() {
		m_jarIteratorFactory = new JarIteratorFactory();
		m_binaryClassesIteratorFactory = new BinaryClassesIteratorFactory();
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactAnalysisIterator createAnalyzeIterator(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler) {
		return getAppropriateFactory(artifactContainerInputPath).createAnalyzeIterator(artifactContainerInputPath,
				exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactModificationIterator createModificationIterator(String artifactContainerInputPath,
			String artifactContainerOutputPath, IArtifactExceptionHandler exceptionHandler) {
		return getAppropriateFactory(artifactContainerInputPath).createModificationIterator(artifactContainerInputPath,
				artifactContainerOutputPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactOutputWriter createArtifactOutputWriter(String artifactContainerPath) {
		return getAppropriateFactory(artifactContainerPath).createArtifactOutputWriter(artifactContainerPath);
	}

	private IArtifactIteratorFactory getAppropriateFactory(String artifactContainerPath) {
		if (isJarArtifact(artifactContainerPath)) {
			return m_jarIteratorFactory;
		}

		return m_binaryClassesIteratorFactory;
	}

	private boolean isJarArtifact(String artifactContainerPath) {
		return artifactContainerPath.endsWith(FileSystemConstants.FILE_EXTENSION_JAR);
	}
}
