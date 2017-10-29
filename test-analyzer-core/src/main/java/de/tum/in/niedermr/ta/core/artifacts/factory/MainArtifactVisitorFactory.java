package de.tum.in.niedermr.ta.core.artifacts.factory;

import de.tum.in.niedermr.ta.core.artifacts.binaryclasses.BinaryClassesVisitorFactory;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.artifacts.jars.JarVisitorFactory;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public final class MainArtifactVisitorFactory extends AbstractArtifactVisitorFactory {

	/** Instance that delegates to the appropriate factory. */
	public static final IArtifactVisitorFactory INSTANCE = new MainArtifactVisitorFactory();

	private final JarVisitorFactory m_jarIteratorFactory;
	private final BinaryClassesVisitorFactory m_binaryClassesIteratorFactory;

	/** Constructor. */
	public MainArtifactVisitorFactory() {
		m_jarIteratorFactory = new JarVisitorFactory();
		m_binaryClassesIteratorFactory = new BinaryClassesVisitorFactory();
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactOutputWriter createArtifactOutputWriter(String artifactContainerPath) {
		return getAppropriateFactory(artifactContainerPath).createArtifactOutputWriter(artifactContainerPath);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactIterator createArtifactIterator(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler) {
		return getAppropriateFactory(artifactContainerInputPath).createArtifactIterator(artifactContainerInputPath,
				exceptionHandler);
	}

	private AbstractArtifactVisitorFactory getAppropriateFactory(String artifactContainerPath) {
		if (isJarArtifact(artifactContainerPath)) {
			return m_jarIteratorFactory;
		}

		return m_binaryClassesIteratorFactory;
	}

	private boolean isJarArtifact(String artifactContainerPath) {
		return artifactContainerPath.endsWith(FileSystemConstants.FILE_EXTENSION_JAR);
	}
}
